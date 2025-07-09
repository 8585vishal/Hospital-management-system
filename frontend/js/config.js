// Application Configuration
const CONFIG = {
    // API Configuration
    API: {
        BASE_URL: 'http://localhost:8080',
        ENDPOINTS: {
            // Patient endpoints
            PATIENTS: '/api/patients',
            PATIENT_BY_ID: '/api/patients/:id',
            PATIENT_SEARCH: '/api/patients/search',
            PATIENT_COUNT: '/api/patients/count',
            PATIENT_BY_GENDER: '/api/patients/gender/:gender',
            
            // Doctor endpoints
            DOCTORS: '/api/doctors',
            DOCTOR_BY_ID: '/api/doctors/:id',
            DOCTOR_SEARCH: '/api/doctors/search',
            DOCTOR_AVAILABLE: '/api/doctors/available',
            DOCTOR_AVAILABLE_COUNT: '/api/doctors/available/count',
            DOCTOR_SPECIALIZATIONS: '/api/doctors/specializations',
            DOCTOR_DEPARTMENTS: '/api/doctors/departments',
            
            // Appointment endpoints
            APPOINTMENTS: '/api/appointments',
            APPOINTMENT_BY_ID: '/api/appointments/:id',
            APPOINTMENT_CREATE: '/api/appointments/create',
            APPOINTMENT_BY_PATIENT: '/api/appointments/patient/:patientId',
            APPOINTMENT_BY_DOCTOR: '/api/appointments/doctor/:doctorId',
            APPOINTMENT_TODAY: '/api/appointments/today',
            APPOINTMENT_UPCOMING: '/api/appointments/upcoming',
            APPOINTMENT_TODAY_COUNT: '/api/appointments/today/count',
            APPOINTMENT_STATUS: '/api/appointments/:id/status',
            
            // Medical Report endpoints
            MEDICAL_REPORTS: '/api/medical-reports',
            MEDICAL_REPORT_BY_ID: '/api/medical-reports/:id',
            MEDICAL_REPORT_CREATE: '/api/medical-reports/create',
            MEDICAL_REPORT_BY_PATIENT: '/api/medical-reports/patient/:patientId',
            MEDICAL_REPORT_BY_DOCTOR: '/api/medical-reports/doctor/:doctorId',
            MEDICAL_REPORT_GENERATE_RANDOM: '/api/medical-reports/generate-random',
            MEDICAL_REPORT_DOWNLOAD: '/api/medical-reports/:id/download',
            
            // Invoice endpoints
            INVOICES: '/api/invoices',
            INVOICE_BY_ID: '/api/invoices/:id',
            INVOICE_CREATE: '/api/invoices/create',
            INVOICE_BY_PATIENT: '/api/invoices/patient/:patientId',
            INVOICE_OUTSTANDING: '/api/invoices/outstanding',
            INVOICE_GENERATE_RANDOM: '/api/invoices/generate-random',
            INVOICE_DOWNLOAD: '/api/invoices/:id/download',
            INVOICE_MARK_PAID: '/api/invoices/:id/mark-paid'
        }
    },
    
    // Application Settings
    APP: {
        NAME: 'MediCare Pro',
        VERSION: '1.0.0',
        LOADING_DELAY: 3000, // 3 seconds
        NOTIFICATION_DURATION: 5000, // 5 seconds
        ANIMATION_DURATION: 300, // 300ms
        DEBOUNCE_DELAY: 500, // 500ms for search
        ITEMS_PER_PAGE: 10,
        MAX_FILE_SIZE: 10 * 1024 * 1024, // 10MB
        SUPPORTED_FILE_TYPES: ['pdf', 'jpg', 'jpeg', 'png', 'doc', 'docx']
    },
    
    // Theme Configuration
    THEME: {
        PRIMARY_COLOR: '#2563eb',
        SECONDARY_COLOR: '#64748b',
        SUCCESS_COLOR: '#10b981',
        WARNING_COLOR: '#f59e0b',
        DANGER_COLOR: '#ef4444',
        SIDEBAR_WIDTH: '280px',
        HEADER_HEIGHT: '70px'
    },
    
    // Default Values
    DEFAULTS: {
        APPOINTMENT_DURATION: 30, // minutes
        CONSULTATION_FEE: 150.00,
        TAX_RATE: 0.10, // 10%
        CURRENCY: 'USD',
        DATE_FORMAT: 'YYYY-MM-DD',
        TIME_FORMAT: 'HH:mm',
        DATETIME_FORMAT: 'YYYY-MM-DD HH:mm'
    },
    
    // Validation Rules
    VALIDATION: {
        PHONE_PATTERN: /^[0-9]{10}$/,
        EMAIL_PATTERN: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
        PASSWORD_MIN_LENGTH: 8,
        NAME_MIN_LENGTH: 2,
        NAME_MAX_LENGTH: 50,
        DESCRIPTION_MAX_LENGTH: 1000
    },
    
    // Error Messages
    MESSAGES: {
        ERROR: {
            GENERIC: 'An error occurred. Please try again.',
            NETWORK: 'Network error. Please check your connection.',
            UNAUTHORIZED: 'You are not authorized to perform this action.',
            FORBIDDEN: 'Access denied.',
            NOT_FOUND: 'Resource not found.',
            VALIDATION: 'Please check your input and try again.',
            SERVER: 'Server error. Please try again later.'
        },
        SUCCESS: {
            SAVE: 'Data saved successfully!',
            UPDATE: 'Data updated successfully!',
            DELETE: 'Data deleted successfully!',
            CREATE: 'Created successfully!',
            UPLOAD: 'File uploaded successfully!'
        },
        VALIDATION: {
            REQUIRED: 'This field is required.',
            EMAIL: 'Please enter a valid email address.',
            PHONE: 'Please enter a valid 10-digit phone number.',
            MIN_LENGTH: 'Minimum {min} characters required.',
            MAX_LENGTH: 'Maximum {max} characters allowed.',
            INVALID: 'Invalid value.'
        }
    },
    
    // Status Options
    STATUS: {
        APPOINTMENT: [
            { value: 'SCHEDULED', label: 'Scheduled', color: '#f59e0b' },
            { value: 'CONFIRMED', label: 'Confirmed', color: '#3b82f6' },
            { value: 'IN_PROGRESS', label: 'In Progress', color: '#8b5cf6' },
            { value: 'COMPLETED', label: 'Completed', color: '#10b981' },
            { value: 'CANCELLED', label: 'Cancelled', color: '#ef4444' },
            { value: 'NO_SHOW', label: 'No Show', color: '#6b7280' }
        ],
        INVOICE: [
            { value: 'PENDING', label: 'Pending', color: '#f59e0b' },
            { value: 'PAID', label: 'Paid', color: '#10b981' },
            { value: 'OVERDUE', label: 'Overdue', color: '#ef4444' },
            { value: 'CANCELLED', label: 'Cancelled', color: '#6b7280' },
            { value: 'REFUNDED', label: 'Refunded', color: '#8b5cf6' }
        ]
    },
    
    // Medical Specializations
    SPECIALIZATIONS: [
        'Cardiology',
        'Dermatology',
        'Emergency Medicine',
        'Endocrinology',
        'Family Medicine',
        'Gastroenterology',
        'General Surgery',
        'Hematology',
        'Infectious Disease',
        'Internal Medicine',
        'Neurology',
        'Obstetrics and Gynecology',
        'Oncology',
        'Ophthalmology',
        'Orthopedics',
        'Otolaryngology',
        'Pathology',
        'Pediatrics',
        'Psychiatry',
        'Pulmonology',
        'Radiology',
        'Rheumatology',
        'Urology'
    ],
    
    // Departments
    DEPARTMENTS: [
        'Emergency',
        'Intensive Care Unit (ICU)',
        'Surgery',
        'Pediatrics',
        'Obstetrics and Gynecology',
        'Cardiology',
        'Neurology',
        'Orthopedics',
        'Radiology',
        'Laboratory',
        'Pharmacy',
        'Administration',
        'Outpatient',
        'Inpatient'
    ],
    
    // Report Types
    REPORT_TYPES: [
        'Blood Test',
        'X-Ray',
        'MRI Scan',
        'CT Scan',
        'Ultrasound',
        'ECG/EKG',
        'Biopsy',
        'Pathology Report',
        'Discharge Summary',
        'Progress Note',
        'Consultation Report',
        'Laboratory Report'
    ],
    
    // Gender Options
    GENDERS: [
        { value: 'Male', label: 'Male' },
        { value: 'Female', label: 'Female' },
        { value: 'Other', label: 'Other' }
    ],
    
    // Common Medical Conditions
    MEDICAL_CONDITIONS: [
        'Diabetes',
        'Hypertension',
        'Heart Disease',
        'Asthma',
        'COPD',
        'Depression',
        'Anxiety',
        'Arthritis',
        'Osteoporosis',
        'Kidney Disease',
        'Liver Disease',
        'Cancer',
        'Stroke',
        'Alzheimer\'s Disease',
        'Parkinson\'s Disease'
    ],
    
    // Payment Methods
    PAYMENT_METHODS: [
        'Cash',
        'Credit Card',
        'Debit Card',
        'Bank Transfer',
        'Insurance',
        'Check',
        'PayPal',
        'Apple Pay',
        'Google Pay'
    ]
};

// Utility function to get full API URL
CONFIG.getApiUrl = function(endpoint, params = {}) {
    let url = this.API.BASE_URL + endpoint;
    
    // Replace URL parameters
    for (const [key, value] of Object.entries(params)) {
        url = url.replace(':' + key, value);
    }
    
    return url;
};

// Utility function to format currency
CONFIG.formatCurrency = function(amount, currency = this.DEFAULTS.CURRENCY) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: currency
    }).format(amount);
};

// Utility function to format date
CONFIG.formatDate = function(date, format = this.DEFAULTS.DATE_FORMAT) {
    if (!date) return '';
    
    const d = new Date(date);
    const options = {};
    
    switch (format) {
        case 'short':
            options.year = 'numeric';
            options.month = 'short';
            options.day = 'numeric';
            break;
        case 'long':
            options.year = 'numeric';
            options.month = 'long';
            options.day = 'numeric';
            break;
        case 'time':
            options.hour = '2-digit';
            options.minute = '2-digit';
            break;
        case 'datetime':
            options.year = 'numeric';
            options.month = 'short';
            options.day = 'numeric';
            options.hour = '2-digit';
            options.minute = '2-digit';
            break;
        default:
            return d.toISOString().split('T')[0];
    }
    
    return d.toLocaleDateString('en-US', options);
};

// Export for module systems if available
if (typeof module !== 'undefined' && module.exports) {
    module.exports = CONFIG;
}

// Make available globally
window.CONFIG = CONFIG;