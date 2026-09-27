package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.DeliveryApplication
import com.example.data.repository.ApplicationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.security.MessageDigest

import com.example.ui.theme.DashboardViewType
import com.example.ui.theme.UiDesignTheme
import com.example.ui.theme.UiLayoutType

data class RegistrationFormState(
    val name: String = "",
    val mobile: String = "",
    val cityHub: String = "Bengaluru Central Hub",
    val bikeAvailable: Boolean = true,
    val vehicleType: String = "Motorcycle / Bike",
    val shiftType: String = "Full-Time Partner",
    val hasDrivingLicense: Boolean = true,
    val experienceLevel: String = "1-2 Years Experience",
    val wizardStep: Int = 1,
    val aadharFrontUri: String? = null,
    val aadharBackUri: String? = null,
    val panFrontUri: String? = null,
    val panBackUri: String? = null,
    val selfieUri: String? = null,
    val consentAccepted: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val successApplication: DeliveryApplication? = null
) {
    val totalDocumentsCount: Int = 5
    val uploadedDocumentsCount: Int
        get() = listOfNotNull(
            aadharFrontUri,
            aadharBackUri,
            panFrontUri,
            panBackUri,
            selfieUri
        ).size

    val progress: Float
        get() = uploadedDocumentsCount.toFloat() / totalDocumentsCount.toFloat()

    val isStep1Valid: Boolean
        get() = name.trim().length >= 2 && Regex("^[6-9]\\d{9}$").matches(mobile)

    val isStep2Valid: Boolean
        get() = vehicleType.isNotBlank() && shiftType.isNotBlank()

    val isStep3Valid: Boolean
        get() = uploadedDocumentsCount == totalDocumentsCount
}

data class AdminAuthState(
    val isAuthenticated: Boolean = false,
    val usernameInput: String = "",
    val passwordInput: String = "",
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)

class DeliveryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ApplicationRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = ApplicationRepository(database.deliveryApplicationDao(), application)
        viewModelScope.launch {
            repository.seedSampleDataIfNeeded()
        }
    }

    // --- UI Design Theme Selection (Ekart Classic, Eco Fleet, Cyber Express, Sunrise Orange, Midnight Dark) ---
    private val _currentUiTheme = MutableStateFlow(UiDesignTheme.EKART_CLASSIC)
    val currentUiTheme: StateFlow<UiDesignTheme> = _currentUiTheme.asStateFlow()

    fun setUiDesignTheme(theme: UiDesignTheme) {
        _currentUiTheme.value = theme
    }

    // --- UI Layout Type (Stepper Wizard, Classic All-in-One, Modular Cards) ---
    private val _currentLayoutType = MutableStateFlow(UiLayoutType.STEPPER_WIZARD)
    val currentLayoutType: StateFlow<UiLayoutType> = _currentLayoutType.asStateFlow()

    fun setUiLayoutType(layout: UiLayoutType) {
        _currentLayoutType.value = layout
    }

    // --- Admin Dashboard View Type (Cards vs Table) ---
    private val _dashboardViewType = MutableStateFlow(DashboardViewType.CARDS)
    val dashboardViewType: StateFlow<DashboardViewType> = _dashboardViewType.asStateFlow()

    fun setDashboardViewType(viewType: DashboardViewType) {
        _dashboardViewType.value = viewType
    }

    // --- UI Style Customization Sheet Visibility ---
    private val _showDesignSheet = MutableStateFlow(false)
    val showDesignSheet: StateFlow<Boolean> = _showDesignSheet.asStateFlow()

    fun toggleDesignSheet(show: Boolean) {
        _showDesignSheet.value = show
    }

    // --- Registration Form State ---
    private val _registrationState = MutableStateFlow(RegistrationFormState())
    val registrationState: StateFlow<RegistrationFormState> = _registrationState.asStateFlow()

    // --- Status Check State ---
    private val _statusQuery = MutableStateFlow("")
    val statusQuery: StateFlow<String> = _statusQuery.asStateFlow()

    private val _statusSearchResults = MutableStateFlow<List<DeliveryApplication>>(emptyList())
    val statusSearchResults: StateFlow<List<DeliveryApplication>> = _statusSearchResults.asStateFlow()

    private val _hasSearchedStatus = MutableStateFlow(false)
    val hasSearchedStatus: StateFlow<Boolean> = _hasSearchedStatus.asStateFlow()

    private val _isSearchingStatus = MutableStateFlow(false)
    val isSearchingStatus: StateFlow<Boolean> = _isSearchingStatus.asStateFlow()

    // --- Admin Dashboard State ---
    private val _adminAuth = MutableStateFlow(AdminAuthState())
    val adminAuth: StateFlow<AdminAuthState> = _adminAuth.asStateFlow()

    private val _adminFilter = MutableStateFlow("ALL")
    val adminFilter: StateFlow<String> = _adminFilter.asStateFlow()

    private val _adminSearchQuery = MutableStateFlow("")
    val adminSearchQuery: StateFlow<String> = _adminSearchQuery.asStateFlow()

    val totalCount = repository.totalCount.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val pendingCount = repository.pendingCount.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val approvedCount = repository.approvedCount.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val rejectedCount = repository.rejectedCount.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val allApplications: StateFlow<List<DeliveryApplication>> = combine(
        repository.allApplications,
        _adminFilter,
        _adminSearchQuery
    ) { apps, filter, query ->
        var filtered = when (filter) {
            "PENDING" -> apps.filter { it.status == DeliveryApplication.STATUS_PENDING }
            "APPROVED" -> apps.filter { it.status == DeliveryApplication.STATUS_APPROVED }
            "REJECTED" -> apps.filter { it.status == DeliveryApplication.STATUS_REJECTED }
            else -> apps
        }
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            filtered = filtered.filter {
                it.name.lowercase().contains(q) ||
                it.mobile.contains(q) ||
                it.applicationId.lowercase().contains(q)
            }
        }
        filtered
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedApplication = MutableStateFlow<DeliveryApplication?>(null)
    val selectedApplication: StateFlow<DeliveryApplication?> = _selectedApplication.asStateFlow()

    private val _zoomPreview = MutableStateFlow<Pair<String, String>?>(null)
    val zoomPreview: StateFlow<Pair<String, String>?> = _zoomPreview.asStateFlow()

    fun updateName(name: String) {
        _registrationState.update { it.copy(name = name, errorMessage = null) }
    }

    fun updateMobile(mobile: String) {
        val digits = mobile.filter { it.isDigit() }.take(10)
        _registrationState.update { it.copy(mobile = digits, errorMessage = null) }
    }

    fun updateBike(available: Boolean) {
        _registrationState.update { it.copy(bikeAvailable = available) }
    }

    fun setVehicleType(type: String) {
        val hasBike = when (type) {
            "Motorcycle / Bike", "EV Electric Scooter", "3-Wheeler Mini Cargo" -> true
            else -> false
        }
        _registrationState.update { it.copy(vehicleType = type, bikeAvailable = hasBike) }
    }

    fun setShiftType(type: String) {
        _registrationState.update { it.copy(shiftType = type) }
    }

    fun setCityHub(hub: String) {
        _registrationState.update { it.copy(cityHub = hub) }
    }

    fun setDrivingLicense(hasLicense: Boolean) {
        _registrationState.update { it.copy(hasDrivingLicense = hasLicense) }
    }

    fun setExperienceLevel(level: String) {
        _registrationState.update { it.copy(experienceLevel = level) }
    }

    fun setWizardStep(step: Int) {
        _registrationState.update { it.copy(wizardStep = step.coerceIn(1, 4), errorMessage = null) }
    }

    fun nextStep(): Boolean {
        val s = _registrationState.value
        when (s.wizardStep) {
            1 -> {
                if (s.name.trim().length < 2) {
                    _registrationState.update { it.copy(errorMessage = "Please enter applicant's full name (min 2 characters).") }
                    return false
                }
                if (!Regex("^[6-9]\\d{9}$").matches(s.mobile)) {
                    _registrationState.update { it.copy(errorMessage = "Please enter a valid 10-digit Indian mobile number.") }
                    return false
                }
                setWizardStep(2)
                return true
            }
            2 -> {
                setWizardStep(3)
                return true
            }
            3 -> {
                if (s.uploadedDocumentsCount < s.totalDocumentsCount) {
                    _registrationState.update { it.copy(errorMessage = "Please upload all 5 required identity documents before proceeding.") }
                    return false
                }
                setWizardStep(4)
                return true
            }
            else -> return true
        }
    }

    fun prevStep() {
        val cur = _registrationState.value.wizardStep
        if (cur > 1) {
            setWizardStep(cur - 1)
        }
    }

    fun updateDocument(docType: String, uri: String) {
        _registrationState.update { state ->
            when (docType) {
                "aadhar_front" -> state.copy(aadharFrontUri = uri, errorMessage = null)
                "aadhar_back" -> state.copy(aadharBackUri = uri, errorMessage = null)
                "pan_front" -> state.copy(panFrontUri = uri, errorMessage = null)
                "pan_back" -> state.copy(panBackUri = uri, errorMessage = null)
                "selfie" -> state.copy(selfieUri = uri, errorMessage = null)
                else -> state
            }
        }
    }

    fun removeDocument(docType: String) {
        _registrationState.update { state ->
            when (docType) {
                "aadhar_front" -> state.copy(aadharFrontUri = null)
                "aadhar_back" -> state.copy(aadharBackUri = null)
                "pan_front" -> state.copy(panFrontUri = null)
                "pan_back" -> state.copy(panBackUri = null)
                "selfie" -> state.copy(selfieUri = null)
                else -> state
            }
        }
    }

    fun updateConsent(accepted: Boolean) {
        _registrationState.update { it.copy(consentAccepted = accepted, errorMessage = null) }
    }

    fun submitApplication(onSuccess: (DeliveryApplication) -> Unit) {
        val state = _registrationState.value

        if (state.name.trim().length < 2) {
            _registrationState.update { it.copy(errorMessage = "Please enter applicant's full name (minimum 2 characters).") }
            return
        }

        val mobileRegex = Regex("^[6-9]\\d{9}$")
        if (!mobileRegex.matches(state.mobile)) {
            _registrationState.update { it.copy(errorMessage = "Please enter a valid 10-digit Indian mobile number starting with 6, 7, 8, or 9.") }
            return
        }

        if (state.aadharFrontUri == null) {
            _registrationState.update { it.copy(errorMessage = "Please upload Aadhar Card Front side photo.") }
            return
        }

        if (state.aadharBackUri == null) {
            _registrationState.update { it.copy(errorMessage = "Please upload Aadhar Card Back side photo.") }
            return
        }

        if (state.panFrontUri == null) {
            _registrationState.update { it.copy(errorMessage = "Please upload PAN Card Front side photo.") }
            return
        }

        if (state.panBackUri == null) {
            _registrationState.update { it.copy(errorMessage = "Please upload PAN Card Back side photo.") }
            return
        }

        if (state.selfieUri == null) {
            _registrationState.update { it.copy(errorMessage = "Please upload applicant Selfie Photo.") }
            return
        }

        if (!state.consentAccepted) {
            _registrationState.update { it.copy(errorMessage = "Please accept the declaration & consent checkbox before submitting.") }
            return
        }

        _registrationState.update { it.copy(isSubmitting = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val app = repository.submitApplication(
                    name = state.name,
                    mobile = state.mobile,
                    bikeAvailable = state.bikeAvailable,
                    aadharFrontUri = state.aadharFrontUri,
                    aadharBackUri = state.aadharBackUri,
                    panFrontUri = state.panFrontUri,
                    panBackUri = state.panBackUri,
                    selfieUri = state.selfieUri
                )
                _registrationState.update {
                    it.copy(
                        isSubmitting = false,
                        successApplication = app
                    )
                }
                onSuccess(app)
            } catch (e: Exception) {
                _registrationState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = "Submission failed: ${e.localizedMessage ?: "Unknown error"}"
                    )
                }
            }
        }
    }

    fun resetRegistrationForm() {
        _registrationState.value = RegistrationFormState()
    }

    fun updateStatusQuery(query: String) {
        _statusQuery.value = query
    }

    fun searchStatus() {
        val query = _statusQuery.value.trim()
        if (query.isBlank()) return

        _isSearchingStatus.value = true
        _hasSearchedStatus.value = true

        viewModelScope.launch {
            repository.searchApplications(query).collect { results ->
                _statusSearchResults.value = results
                _isSearchingStatus.value = false
            }
        }
    }

    fun updateAdminUsername(username: String) {
        _adminAuth.update { it.copy(usernameInput = username, errorMessage = null) }
    }

    fun updateAdminPassword(password: String) {
        _adminAuth.update { it.copy(passwordInput = password, errorMessage = null) }
    }

    fun loginAdmin(onSuccess: () -> Unit) {
        val state = _adminAuth.value
        val username = state.usernameInput.trim()
        val password = state.passwordInput.trim()

        if (username.isEmpty() || password.isEmpty()) {
            _adminAuth.update { it.copy(errorMessage = "Please enter both username and password.") }
            return
        }

        val isUserValid = username.equals("Admin", ignoreCase = true)
        val isPassValid = verifyAdminPassword(password)

        if (isUserValid && isPassValid) {
            _adminAuth.update {
                it.copy(
                    isAuthenticated = true,
                    errorMessage = null,
                    passwordInput = ""
                )
            }
            onSuccess()
        } else {
            _adminAuth.update {
                it.copy(
                    errorMessage = "Invalid credentials. Please verify your Admin username and password."
                )
            }
        }
    }

    private fun verifyAdminPassword(input: String): Boolean {
        val expectedHash = hashSha256("850913")
        val inputHash = hashSha256(input)
        return expectedHash == inputHash
    }

    private fun hashSha256(str: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(str.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun logoutAdmin() {
        _adminAuth.value = AdminAuthState()
        _selectedApplication.value = null
    }

    fun setAdminFilter(filter: String) {
        _adminFilter.value = filter
    }

    fun setAdminSearchQuery(query: String) {
        _adminSearchQuery.value = query
    }

    fun selectApplication(application: DeliveryApplication?) {
        _selectedApplication.value = application
    }

    fun approveApplication(applicationId: String, onDone: () -> Unit) {
        viewModelScope.launch {
            repository.approveApplication(applicationId)
            repository.getApplicationById(applicationId).collect { updated ->
                _selectedApplication.value = updated
                onDone()
            }
        }
    }

    fun rejectApplication(applicationId: String, reason: String, onDone: () -> Unit) {
        viewModelScope.launch {
            repository.rejectApplication(applicationId, reason)
            repository.getApplicationById(applicationId).collect { updated ->
                _selectedApplication.value = updated
                onDone()
            }
        }
    }

    fun openZoom(title: String, uriOrPath: String) {
        _zoomPreview.value = Pair(title, uriOrPath)
    }

    fun closeZoom() {
        _zoomPreview.value = null
    }
}
