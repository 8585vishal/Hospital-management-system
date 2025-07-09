// Main Application Entry Point
class HospitalApp {
    constructor() {
        this.currentSection = 'dashboard';
        this.isLoading = false;
        this.init();
    }

    async init() {
        this.showLoadingScreen();
        this.initializeEventListeners();
        this.initializeAOS();
        await this.loadInitialData();
        this.hideLoadingScreen();
    }

    showLoadingScreen() {
        const loadingScreen = document.getElementById('loading-screen');
        if (loadingScreen) {
            loadingScreen.classList.remove('hidden');
        }
    }

    hideLoadingScreen() {
        setTimeout(() => {
            const loadingScreen = document.getElementById('loading-screen');
            if (loadingScreen) {
                loadingScreen.classList.add('hidden');
            }
        }, CONFIG.APP.LOADING_DELAY);
    }

    initializeAOS() {
        if (typeof AOS !== 'undefined') {
            AOS.init({
                duration: 800,
                easing: 'ease-in-out',
                once: true,
                offset: 100
            });
        }
    }

    initializeEventListeners() {
        // Navigation
        this.initNavigation();
        
        // Sidebar toggle
        this.initSidebarToggle();
        
        // Global search
        this.initGlobalSearch();
        
        // Quick actions
        this.initQuickActions();
        
        // Modal handling
        this.initModals();
        
        // Button event listeners
        this.initButtonHandlers();
    }

    initNavigation() {
        const navLinks = document.querySelectorAll('.nav-link');
        navLinks.forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                const section = link.dataset.section;
                this.navigateToSection(section);
            });
        });
    }

    navigateToSection(section) {
        // Update active nav item
        document.querySelectorAll('.nav-item').forEach(item => {
            item.classList.remove('active');
        });
        document.querySelector(`[data-section="${section}"]`).parentElement.classList.add('active');

        // Hide all sections
        document.querySelectorAll('.content-section').forEach(sec => {
            sec.classList.remove('active');
        });

        // Show target section
        const targetSection = document.getElementById(`${section}-section`);
        if (targetSection) {
            targetSection.classList.add('active');
        }

        // Update page title
        const pageTitle = document.getElementById('pageTitle');
        if (pageTitle) {
            pageTitle.textContent = this.getSectionTitle(section);
        }

        this.currentSection = section;
        this.loadSectionData(section);
    }

    getSectionTitle(section) {
        const titles = {
            'dashboard': 'Dashboard',
            'patients': 'Patient Management',
            'doctors': 'Doctor Management',
            'appointments': 'Appointments',
            'medical-reports': 'Medical Reports',
            'billing': 'Billing & Invoices',
            'analytics': 'Analytics & Reporting'
        };
        return titles[section] || 'Dashboard';
    }

    initSidebarToggle() {
        const toggleButtons = document.querySelectorAll('.sidebar-toggle, .sidebar-toggle-main');
        const sidebar = document.getElementById('sidebar');
        const mainContent = document.getElementById('mainContent');

        toggleButtons.forEach(button => {
            button.addEventListener('click', () => {
                if (window.innerWidth <= 768) {
                    sidebar.classList.toggle('open');
                } else {
                    sidebar.classList.toggle('collapsed');
                    mainContent.classList.toggle('sidebar-collapsed');
                }
            });
        });

        // Close sidebar on mobile when clicking outside
        document.addEventListener('click', (e) => {
            if (window.innerWidth <= 768 && 
                !sidebar.contains(e.target) && 
                !e.target.classList.contains('sidebar-toggle-main')) {
                sidebar.classList.remove('open');
            }
        });
    }

    initGlobalSearch() {
        const searchInput = document.getElementById('globalSearch');
        if (searchInput) {
            let searchTimeout;
            searchInput.addEventListener('input', (e) => {
                clearTimeout(searchTimeout);
                searchTimeout = setTimeout(() => {
                    this.performGlobalSearch(e.target.value);
                }, CONFIG.APP.DEBOUNCE_DELAY);
            });
        }
    }

    async performGlobalSearch(query) {
        if (query.length < 2) return;

        try {
            // Search patients and doctors
            const [patientsResult, doctorsResult] = await Promise.all([
                api.searchPatients(query),
                api.searchDoctors(query)
            ]);

            // Display search results (implement as needed)
            console.log('Search results:', { 
                patients: patientsResult.data, 
                doctors: doctorsResult.data 
            });
        } catch (error) {
            console.error('Search failed:', error);
        }
    }

    initQuickActions() {
        const quickActionBtns = document.querySelectorAll('.quick-action-btn');
        quickActionBtns.forEach(btn => {
            btn.addEventListener('click', () => {
                const action = btn.dataset.action;
                this.handleQuickAction(action);
            });
        });
    }

    handleQuickAction(action) {
        switch (action) {
            case 'add-patient':
                this.showModal('patientModal');
                break;
            case 'schedule-appointment':
                this.navigateToSection('appointments');
                break;
            case 'generate-report':
                this.generateRandomReport();
                break;
            case 'create-invoice':
                this.generateRandomInvoice();
                break;
            default:
                console.log('Unknown action:', action);
        }
    }

    initModals() {
        // Close modals when clicking outside
        document.addEventListener('click', (e) => {
            if (e.target.classList.contains('modal')) {
                this.closeModal(e.target.id);
            }
        });

        // ESC key to close modals
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape') {
                this.closeAllModals();
            }
        });
    }

    showModal(modalId) {
        const modal = document.getElementById(modalId);
        if (modal) {
            modal.classList.add('active');
        }
    }

    closeModal(modalId) {
        const modal = document.getElementById(modalId);
        if (modal) {
            modal.classList.remove('active');
        }
    }

    closeAllModals() {
        document.querySelectorAll('.modal.active').forEach(modal => {
            modal.classList.remove('active');
        });
    }

    initButtonHandlers() {
        // Generate random report button
        const generateReportBtn = document.getElementById('generateRandomReportBtn');
        if (generateReportBtn) {
            generateReportBtn.addEventListener('click', () => {
                this.generateRandomReport();
            });
        }

        // Generate random invoice button
        const generateInvoiceBtn = document.getElementById('generateRandomInvoiceBtn');
        if (generateInvoiceBtn) {
            generateInvoiceBtn.addEventListener('click', () => {
                this.generateRandomInvoice();
            });
        }

        // Add patient button
        const addPatientBtn = document.getElementById('addPatientBtn');
        if (addPatientBtn) {
            addPatientBtn.addEventListener('click', () => {
                this.showModal('patientModal');
            });
        }
    }

    async generateRandomReport() {
        try {
            this.showLoading('Generating random medical report...');
            const result = await api.generateRandomReport();
            this.hideLoading();
            this.showNotification('Random medical report generated successfully!', 'success');
            
            // Reload reports if we're on that section
            if (this.currentSection === 'medical-reports') {
                this.loadMedicalReports();
            }
        } catch (error) {
            this.hideLoading();
            this.showNotification('Failed to generate report: ' + error.message, 'error');
        }
    }

    async generateRandomInvoice() {
        try {
            this.showLoading('Generating random invoice...');
            const result = await api.generateRandomInvoice();
            this.hideLoading();
            this.showNotification('Random invoice generated successfully!', 'success');
            
            // Reload invoices if we're on that section
            if (this.currentSection === 'billing') {
                this.loadInvoices();
            }
        } catch (error) {
            this.hideLoading();
            this.showNotification('Failed to generate invoice: ' + error.message, 'error');
        }
    }

    async loadInitialData() {
        try {
            await this.loadDashboardData();
        } catch (error) {
            console.error('Failed to load initial data:', error);
        }
    }

    async loadSectionData(section) {
        switch (section) {
            case 'dashboard':
                await this.loadDashboardData();
                break;
            case 'patients':
                await this.loadPatients();
                break;
            case 'doctors':
                await this.loadDoctors();
                break;
            case 'medical-reports':
                await this.loadMedicalReports();
                break;
            case 'billing':
                await this.loadInvoices();
                break;
        }
    }

    async loadDashboardData() {
        try {
            const [patientCount, doctorCount, todayAppointments] = await Promise.all([
                api.getPatientCount(),
                api.getAvailableDoctorCount(),
                api.getTodayAppointmentCount()
            ]);

            // Update dashboard stats
            this.updateElement('totalPatients', patientCount.data);
            this.updateElement('totalDoctors', doctorCount.data);
            this.updateElement('todayAppointments', todayAppointments.data);
            
            // Load recent and upcoming appointments
            await this.loadDashboardAppointments();
        } catch (error) {
            console.error('Failed to load dashboard data:', error);
        }
    }

    async loadDashboardAppointments() {
        try {
            const [recentAppointments, upcomingAppointments] = await Promise.all([
                api.getTodayAppointments(),
                api.getUpcomingAppointments()
            ]);

            this.renderAppointmentsList('recentAppointmentsList', recentAppointments.data.slice(0, 5));
            this.renderAppointmentsList('upcomingAppointmentsList', upcomingAppointments.data.slice(0, 5));
        } catch (error) {
            console.error('Failed to load appointments:', error);
        }
    }

    renderAppointmentsList(containerId, appointments) {
        const container = document.getElementById(containerId);
        if (!container) return;

        if (appointments.length === 0) {
            container.innerHTML = '<p class="text-muted">No appointments found</p>';
            return;
        }

        container.innerHTML = appointments.map(appointment => `
            <div class="appointment-item">
                <div class="appointment-info">
                    <h4>${appointment.patient?.firstName} ${appointment.patient?.lastName}</h4>
                    <p>Dr. ${appointment.doctor?.firstName} ${appointment.doctor?.lastName}</p>
                    <span class="appointment-time">${CONFIG.formatDate(appointment.appointmentDate, 'datetime')}</span>
                </div>
                <span class="appointment-status status-${appointment.status.toLowerCase()}">${appointment.status}</span>
            </div>
        `).join('');
    }

    async loadPatients() {
        try {
            const result = await api.getPatients();
            this.renderPatientsTable(result.data);
        } catch (error) {
            console.error('Failed to load patients:', error);
            this.showNotification('Failed to load patients', 'error');
        }
    }

    async loadDoctors() {
        try {
            const result = await api.getDoctors();
            this.renderDoctorsTable(result.data);
        } catch (error) {
            console.error('Failed to load doctors:', error);
            this.showNotification('Failed to load doctors', 'error');
        }
    }

    async loadMedicalReports() {
        try {
            const result = await api.getMedicalReports();
            this.renderReportsTable(result.data);
        } catch (error) {
            console.error('Failed to load medical reports:', error);
            this.showNotification('Failed to load medical reports', 'error');
        }
    }

    async loadInvoices() {
        try {
            const result = await api.getInvoices();
            this.renderInvoicesTable(result.data);
        } catch (error) {
            console.error('Failed to load invoices:', error);
            this.showNotification('Failed to load invoices', 'error');
        }
    }

    renderPatientsTable(patients) {
        const tbody = document.getElementById('patientsTableBody');
        if (!tbody) return;

        tbody.innerHTML = patients.map(patient => `
            <tr>
                <td>${patient.id}</td>
                <td>${patient.firstName} ${patient.lastName}</td>
                <td>${patient.email || 'N/A'}</td>
                <td>${patient.phone}</td>
                <td>${patient.gender || 'N/A'}</td>
                <td>${patient.dateOfBirth ? CONFIG.formatDate(patient.dateOfBirth) : 'N/A'}</td>
                <td>
                    <div class="action-buttons">
                        <button class="action-btn-sm edit" onclick="app.editPatient(${patient.id})">Edit</button>
                        <button class="action-btn-sm delete" onclick="app.deletePatient(${patient.id})">Delete</button>
                    </div>
                </td>
            </tr>
        `).join('');
    }

    renderDoctorsTable(doctors) {
        const tbody = document.getElementById('doctorsTableBody');
        if (!tbody) return;

        tbody.innerHTML = doctors.map(doctor => `
            <tr>
                <td>${doctor.id}</td>
                <td>Dr. ${doctor.firstName} ${doctor.lastName}</td>
                <td>${doctor.specialization}</td>
                <td>${doctor.department || 'N/A'}</td>
                <td>${doctor.experienceYears || 'N/A'} years</td>
                <td>
                    <span class="status-indicator ${doctor.isAvailable ? 'online' : 'offline'}"></span>
                    ${doctor.isAvailable ? 'Available' : 'Not Available'}
                </td>
                <td>
                    <div class="action-buttons">
                        <button class="action-btn-sm edit" onclick="app.editDoctor(${doctor.id})">Edit</button>
                        <button class="action-btn-sm delete" onclick="app.deleteDoctor(${doctor.id})">Delete</button>
                    </div>
                </td>
            </tr>
        `).join('');
    }

    renderReportsTable(reports) {
        const tbody = document.getElementById('reportsTableBody');
        if (!tbody) return;

        tbody.innerHTML = reports.map(report => `
            <tr>
                <td>${report.id}</td>
                <td>${report.patient?.firstName} ${report.patient?.lastName}</td>
                <td>Dr. ${report.doctor?.firstName} ${report.doctor?.lastName}</td>
                <td>${report.reportType}</td>
                <td>${report.title}</td>
                <td>${CONFIG.formatDate(report.reportDate, 'short')}</td>
                <td>
                    <div class="action-buttons">
                        <button class="action-btn-sm download" onclick="app.downloadReport(${report.id})">Download</button>
                        <button class="action-btn-sm edit" onclick="app.editReport(${report.id})">Edit</button>
                        <button class="action-btn-sm delete" onclick="app.deleteReport(${report.id})">Delete</button>
                    </div>
                </td>
            </tr>
        `).join('');
    }

    renderInvoicesTable(invoices) {
        const tbody = document.getElementById('invoicesTableBody');
        if (!tbody) return;

        tbody.innerHTML = invoices.map(invoice => `
            <tr>
                <td>${invoice.invoiceNumber}</td>
                <td>${invoice.patient?.firstName} ${invoice.patient?.lastName}</td>
                <td>${CONFIG.formatCurrency(invoice.totalAmount)}</td>
                <td>
                    <span class="status-${invoice.status.toLowerCase()}">${invoice.status}</span>
                </td>
                <td>${CONFIG.formatDate(invoice.createdAt, 'short')}</td>
                <td>
                    <div class="action-buttons">
                        <button class="action-btn-sm download" onclick="app.downloadInvoice(${invoice.id})">Download</button>
                        ${invoice.status === 'PENDING' ? 
                            `<button class="action-btn-sm edit" onclick="app.markAsPaid(${invoice.id})">Mark Paid</button>` : 
                            ''
                        }
                        <button class="action-btn-sm delete" onclick="app.deleteInvoice(${invoice.id})">Delete</button>
                    </div>
                </td>
            </tr>
        `).join('');
    }

    // Utility methods
    updateElement(id, value) {
        const element = document.getElementById(id);
        if (element) {
            element.textContent = value;
        }
    }

    showLoading(message = 'Loading...') {
        // Implementation for showing loading spinner
        console.log('Loading:', message);
    }

    hideLoading() {
        // Implementation for hiding loading spinner
        console.log('Loading hidden');
    }

    showNotification(message, type = 'info') {
        // Simple notification implementation
        const notification = document.createElement('div');
        notification.className = `notification ${type}`;
        notification.textContent = message;
        notification.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            padding: 12px 24px;
            border-radius: 8px;
            color: white;
            background: ${type === 'success' ? '#10b981' : type === 'error' ? '#ef4444' : '#3b82f6'};
            z-index: 10000;
            animation: slideInFromRight 0.3s ease;
        `;
        
        document.body.appendChild(notification);
        
        setTimeout(() => {
            notification.remove();
        }, CONFIG.APP.NOTIFICATION_DURATION);
    }

    // Action methods (placeholder implementations)
    async downloadReport(id) {
        try {
            await api.downloadMedicalReport(id);
            this.showNotification('Report downloaded successfully!', 'success');
        } catch (error) {
            this.showNotification('Failed to download report', 'error');
        }
    }

    async downloadInvoice(id) {
        try {
            await api.downloadInvoice(id);
            this.showNotification('Invoice downloaded successfully!', 'success');
        } catch (error) {
            this.showNotification('Failed to download invoice', 'error');
        }
    }

    // Placeholder methods for CRUD operations
    editPatient(id) { console.log('Edit patient:', id); }
    deletePatient(id) { console.log('Delete patient:', id); }
    editDoctor(id) { console.log('Edit doctor:', id); }
    deleteDoctor(id) { console.log('Delete doctor:', id); }
    editReport(id) { console.log('Edit report:', id); }
    deleteReport(id) { console.log('Delete report:', id); }
    deleteInvoice(id) { console.log('Delete invoice:', id); }
    markAsPaid(id) { console.log('Mark invoice as paid:', id); }
}

// Global functions for modal handling
function closeModal(modalId) {
    app.closeModal(modalId);
}

// Initialize app when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    window.app = new HospitalApp();
});