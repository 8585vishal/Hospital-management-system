// API Service for Hospital Management System
class ApiService {
    constructor() {
        this.baseURL = CONFIG.API.BASE_URL;
        this.defaultHeaders = {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        };
    }

    // Generic HTTP request method
    async request(url, options = {}) {
        const config = {
            headers: { ...this.defaultHeaders, ...options.headers },
            ...options
        };

        try {
            const response = await fetch(url, config);
            
            // Handle different response types
            const contentType = response.headers.get('content-type');
            let data;
            
            if (contentType && contentType.includes('application/json')) {
                data = await response.json();
            } else if (contentType && contentType.includes('application/pdf')) {
                data = await response.blob();
            } else {
                data = await response.text();
            }

            if (!response.ok) {
                throw new Error(data.message || `HTTP error! status: ${response.status}`);
            }

            return { data, status: response.status, headers: response.headers };
        } catch (error) {
            console.error('API request failed:', error);
            throw this.handleError(error);
        }
    }

    // HTTP methods
    async get(endpoint, params = {}) {
        const url = new URL(CONFIG.getApiUrl(endpoint, params));
        return this.request(url.toString());
    }

    async post(endpoint, data = {}, params = {}) {
        const url = CONFIG.getApiUrl(endpoint, params);
        return this.request(url, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    }

    async put(endpoint, data = {}, params = {}) {
        const url = CONFIG.getApiUrl(endpoint, params);
        return this.request(url, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    }

    async delete(endpoint, params = {}) {
        const url = CONFIG.getApiUrl(endpoint, params);
        return this.request(url, {
            method: 'DELETE'
        });
    }

    // File download method
    async download(endpoint, params = {}, filename = null) {
        const url = CONFIG.getApiUrl(endpoint, params);
        const response = await this.request(url);
        
        if (response.data instanceof Blob) {
            const blob = response.data;
            const downloadUrl = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = downloadUrl;
            link.download = filename || 'download';
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            window.URL.revokeObjectURL(downloadUrl);
        }
        
        return response;
    }

    // Error handling
    handleError(error) {
        if (error.name === 'TypeError' && error.message.includes('fetch')) {
            return new Error(CONFIG.MESSAGES.ERROR.NETWORK);
        }
        
        switch (error.status) {
            case 401:
                return new Error(CONFIG.MESSAGES.ERROR.UNAUTHORIZED);
            case 403:
                return new Error(CONFIG.MESSAGES.ERROR.FORBIDDEN);
            case 404:
                return new Error(CONFIG.MESSAGES.ERROR.NOT_FOUND);
            case 422:
                return new Error(CONFIG.MESSAGES.ERROR.VALIDATION);
            case 500:
                return new Error(CONFIG.MESSAGES.ERROR.SERVER);
            default:
                return error;
        }
    }

    // Patient API methods
    async getPatients() {
        return this.get(CONFIG.API.ENDPOINTS.PATIENTS);
    }

    async getPatient(id) {
        return this.get(CONFIG.API.ENDPOINTS.PATIENT_BY_ID, { id });
    }

    async createPatient(patientData) {
        return this.post(CONFIG.API.ENDPOINTS.PATIENTS, patientData);
    }

    async updatePatient(id, patientData) {
        return this.put(CONFIG.API.ENDPOINTS.PATIENT_BY_ID, patientData, { id });
    }

    async deletePatient(id) {
        return this.delete(CONFIG.API.ENDPOINTS.PATIENT_BY_ID, { id });
    }

    async searchPatients(query) {
        const url = new URL(CONFIG.getApiUrl(CONFIG.API.ENDPOINTS.PATIENT_SEARCH));
        url.searchParams.append('query', query);
        return this.request(url.toString());
    }

    async getPatientCount() {
        return this.get(CONFIG.API.ENDPOINTS.PATIENT_COUNT);
    }

    // Doctor API methods
    async getDoctors() {
        return this.get(CONFIG.API.ENDPOINTS.DOCTORS);
    }

    async getDoctor(id) {
        return this.get(CONFIG.API.ENDPOINTS.DOCTOR_BY_ID, { id });
    }

    async createDoctor(doctorData) {
        return this.post(CONFIG.API.ENDPOINTS.DOCTORS, doctorData);
    }

    async updateDoctor(id, doctorData) {
        return this.put(CONFIG.API.ENDPOINTS.DOCTOR_BY_ID, doctorData, { id });
    }

    async deleteDoctor(id) {
        return this.delete(CONFIG.API.ENDPOINTS.DOCTOR_BY_ID, { id });
    }

    async getAvailableDoctors() {
        return this.get(CONFIG.API.ENDPOINTS.DOCTOR_AVAILABLE);
    }

    async getAvailableDoctorCount() {
        return this.get(CONFIG.API.ENDPOINTS.DOCTOR_AVAILABLE_COUNT);
    }

    async getDoctorSpecializations() {
        return this.get(CONFIG.API.ENDPOINTS.DOCTOR_SPECIALIZATIONS);
    }

    // Appointment API methods
    async getAppointments() {
        return this.get(CONFIG.API.ENDPOINTS.APPOINTMENTS);
    }

    async getAppointment(id) {
        return this.get(CONFIG.API.ENDPOINTS.APPOINTMENT_BY_ID, { id });
    }

    async createAppointment(appointmentData) {
        return this.post(CONFIG.API.ENDPOINTS.APPOINTMENTS, appointmentData);
    }

    async updateAppointment(id, appointmentData) {
        return this.put(CONFIG.API.ENDPOINTS.APPOINTMENT_BY_ID, appointmentData, { id });
    }

    async deleteAppointment(id) {
        return this.delete(CONFIG.API.ENDPOINTS.APPOINTMENT_BY_ID, { id });
    }

    async getTodayAppointments() {
        return this.get(CONFIG.API.ENDPOINTS.APPOINTMENT_TODAY);
    }

    async getUpcomingAppointments() {
        return this.get(CONFIG.API.ENDPOINTS.APPOINTMENT_UPCOMING);
    }

    async getTodayAppointmentCount() {
        return this.get(CONFIG.API.ENDPOINTS.APPOINTMENT_TODAY_COUNT);
    }

    async updateAppointmentStatus(id, status) {
        const url = new URL(CONFIG.getApiUrl(CONFIG.API.ENDPOINTS.APPOINTMENT_STATUS, { id }));
        url.searchParams.append('status', status);
        return this.request(url.toString(), { method: 'PUT' });
    }

    // Medical Report API methods
    async getMedicalReports() {
        return this.get(CONFIG.API.ENDPOINTS.MEDICAL_REPORTS);
    }

    async getMedicalReport(id) {
        return this.get(CONFIG.API.ENDPOINTS.MEDICAL_REPORT_BY_ID, { id });
    }

    async createMedicalReport(reportData) {
        return this.post(CONFIG.API.ENDPOINTS.MEDICAL_REPORTS, reportData);
    }

    async updateMedicalReport(id, reportData) {
        return this.put(CONFIG.API.ENDPOINTS.MEDICAL_REPORT_BY_ID, reportData, { id });
    }

    async deleteMedicalReport(id) {
        return this.delete(CONFIG.API.ENDPOINTS.MEDICAL_REPORT_BY_ID, { id });
    }

    async generateRandomReport() {
        return this.post(CONFIG.API.ENDPOINTS.MEDICAL_REPORT_GENERATE_RANDOM);
    }

    async downloadMedicalReport(id) {
        return this.download(CONFIG.API.ENDPOINTS.MEDICAL_REPORT_DOWNLOAD, { id }, `medical-report-${id}.pdf`);
    }

    // Invoice API methods
    async getInvoices() {
        return this.get(CONFIG.API.ENDPOINTS.INVOICES);
    }

    async getInvoice(id) {
        return this.get(CONFIG.API.ENDPOINTS.INVOICE_BY_ID, { id });
    }

    async createInvoice(invoiceData) {
        return this.post(CONFIG.API.ENDPOINTS.INVOICES, invoiceData);
    }

    async updateInvoice(id, invoiceData) {
        return this.put(CONFIG.API.ENDPOINTS.INVOICE_BY_ID, invoiceData, { id });
    }

    async deleteInvoice(id) {
        return this.delete(CONFIG.API.ENDPOINTS.INVOICE_BY_ID, { id });
    }

    async generateRandomInvoice() {
        return this.post(CONFIG.API.ENDPOINTS.INVOICE_GENERATE_RANDOM);
    }

    async downloadInvoice(id) {
        return this.download(CONFIG.API.ENDPOINTS.INVOICE_DOWNLOAD, { id }, `invoice-${id}.pdf`);
    }

    async markInvoiceAsPaid(id, paymentMethod) {
        const url = new URL(CONFIG.getApiUrl(CONFIG.API.ENDPOINTS.INVOICE_MARK_PAID, { id }));
        url.searchParams.append('paymentMethod', paymentMethod);
        return this.request(url.toString(), { method: 'PUT' });
    }

    async getTotalOutstanding() {
        return this.get(CONFIG.API.ENDPOINTS.INVOICE_OUTSTANDING);
    }
}

// Create global API instance
const api = new ApiService();

// Export for module systems if available
if (typeof module !== 'undefined' && module.exports) {
    module.exports = { ApiService, api };
}

// Make available globally
window.api = api;