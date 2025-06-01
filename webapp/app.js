// Konfiguracja API
const API_BASE = 'http://localhost:8080/api'; // Dostosuj do swojego API
let currentUser = null;
let authToken = null;

// Inicjalizacja aplikacji
document.addEventListener('DOMContentLoaded', function() {
    setupEventListeners();
    checkAuthStatus();
});

function setupEventListeners() {
    // Formularze
    document.getElementById('loginForm').addEventListener('submit', handleLogin);
    document.getElementById('registerForm').addEventListener('submit', handleRegister);
    document.getElementById('createMeetupForm').addEventListener('submit', handleCreateMeetup);
    document.getElementById('createGroupForm').addEventListener('submit', handleCreateGroup);

    // Zamykanie modali kliknięciem w tło
    document.querySelectorAll('.modal').forEach(modal => {
        modal.addEventListener('click', function(e) {
            if (e.target === modal) {
                closeModal(modal.id);
            }
        });
    });
}

// Funkcje uwierzytelniania
async function handleLogin(e) {
    e.preventDefault();
    const username = document.getElementById('loginUsername').value;
    const password = document.getElementById('loginPassword').value;

    try {
        showMessage('Logowanie...', 'info');

        // Symulacja logowania (zastąp prawdziwym wywołaniem API)
        const response = await simulateApiCall('/login', {
            method: 'POST',
            body: { username, password }
        });

        if (response.success) {
            authToken = response.token;
            currentUser = response.user;
            showApp();
            showMessage('Zalogowano pomyślnie!', 'success');
        } else {
            showMessage('Błąd logowania: ' + response.message, 'error');
        }
    } catch (error) {
        showMessage('Błąd połączenia z serwerem', 'error');
    }
}

async function handleRegister(e) {
    e.preventDefault();
    const username = document.getElementById('registerUsername').value;
    const email = document.getElementById('registerEmail').value;
    const password = document.getElementById('registerPassword').value;

    try {
        showMessage('Rejestracja...', 'info');

        // Symulacja rejestracji
        const response = await simulateApiCall('/register', {
            method: 'POST',
            body: { username, email, password }
        });

        if (response.success) {
            showMessage('Konto utworzone pomyślnie! Możesz się teraz zalogować.', 'success');
            switchTab('login');
        } else {
            showMessage('Błąd rejestracji: ' + response.message, 'error');
        }
    } catch (error) {
        showMessage('Błąd połączenia z serwerem', 'error');
    }
}

function logout() {
    authToken = null;
    currentUser = null;
    showAuth();
    showMessage('Wylogowano pomyślnie', 'success');
}

// Funkcje interfejsu
function switchTab(tabName) {
    document.querySelectorAll('.tab').forEach(tab => tab.classList.remove('active'));
    document.querySelectorAll('.tab-content').forEach(content => content.classList.remove('active'));

    event.target.classList.add('active');
    document.getElementById(tabName + 'Tab').classList.add('active');
}

function switchAppTab(tabName) {
    document.querySelectorAll('#appSection .tab').forEach(tab => tab.classList.remove('active'));
    document.querySelectorAll('#appSection .tab-content').forEach(content => content.classList.remove('active'));

    event.target.classList.add('active');
    document.getElementById(tabName + 'Tab').classList.add('active');

    // Ładowanie danych dla aktywnej zakładki
    if (tabName === 'meetups') {
        loadMeetups();
    } else if (tabName === 'groups') {
        loadGroups();
    } else if (tabName === 'discover') {
        loadDiscoverContent();
    }
}

function switchMeetupTab(type) {
    const buttons = document.querySelectorAll('#meetupsTab .tab');
    buttons.forEach(btn => btn.classList.remove('active'));
    event.target.classList.add('active');

    document.getElementById('organizedMeetups').classList.toggle('hidden', type !== 'organized');
    document.getElementById('joinedMeetups').classList.toggle('hidden', type !== 'joined');

    if (type === 'organized') {
        loadOrganizedMeetups();
    } else {
        loadJoinedMeetups();
    }
}

function switchGroupTab(type) {
    const buttons = document.querySelectorAll('#groupsTab .tab');
    buttons.forEach(btn => btn.classList.remove('active'));
    event.target.classList.add('active');

    document.getElementById('ownedGroups').classList.toggle('hidden', type !== 'owned');
    document.getElementById('joinedGroups').classList.toggle('hidden', type !== 'joined');

    if (type === 'owned') {
        loadOwnedGroups();
    } else {
        loadJoinedGroups();
    }
}

function switchDiscoverTab(type) {
    const buttons = document.querySelectorAll('#discoverTab .tab');
    buttons.forEach(btn => btn.classList.remove('active'));
    event.target.classList.add('active');

    document.getElementById('discoverMeetups').classList.toggle('hidden', type !== 'meetups');
    document.getElementById('discoverGroups').classList.toggle('hidden', type !== 'groups');

    if (type === 'meetups') {
        loadAllMeetups();
    } else {
        loadAllGroups();
    }
}

function showAuth() {
    document.getElementById('authSection').classList.remove('hidden');
    document.getElementById('appSection').classList.add('hidden');
    document.getElementById('userInfo').innerHTML = `
        <button class="btn btn-secondary" onclick="showLogin()">Zaloguj się</button>
        <button class="btn btn-primary" onclick="showRegister()">Zarejestruj się</button>
    `;
}

function showApp() {
    document.getElementById('authSection').classList.add('hidden');
    document.getElementById('appSection').classList.remove('hidden');
    document.getElementById('userInfo').innerHTML = `
        <span>Witaj, ${currentUser?.username || 'Użytkownik'}!</span>
        <button class="btn btn-secondary" onclick="logout()">Wyloguj</button>
    `;
    loadMeetups();
}

function showLogin() {
    switchTab('login');
}

function showRegister() {
    switchTab('register');
}

function showModal(modalId) {
    document.getElementById(modalId).style.display = 'block';
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

function showCreateMeetup() {
    showModal('createMeetupModal');
}

function showCreateGroup() {
    showModal('createGroupModal');
}

// Funkcje ładowania danych
async function loadMeetups() {
    loadOrganizedMeetups();
}

async function loadOrganizedMeetups() {
    const container = document.getElementById('organizedMeetups');
    container.innerHTML = '<div class="loading"><div class="spinner"></div>Ładowanie spotkań...</div>';

    try {
        const meetups = await simulateApiCall('/meetups/organized');
        renderMeetups(meetups, container, true);
    } catch (error) {
        container.innerHTML = '<div class="error">Błąd ładowania spotkań</div>';
    }
}

async function loadJoinedMeetups() {
    const container = document.getElementById('joinedMeetups');
    container.innerHTML = '<div class="loading"><div class="spinner"></div>Ładowanie spotkań...</div>';

    try {
        const meetups = await simulateApiCall('/meetups/joined');
        renderMeetups(meetups, container, false);
    } catch (error) {
        container.innerHTML = '<div class="error">Błąd ładowania spotkań</div>';
    }
}

async function loadGroups() {
    loadOwnedGroups();
}

async function loadOwnedGroups() {
    const container = document.getElementById('ownedGroups');
    container.innerHTML = '<div class="loading"><div class="spinner"></div>Ładowanie grup...</div>';

    try {
        const groups = await simulateApiCall('/groups/owned');
        renderGroups(groups, container, true);
    } catch (error) {
        container.innerHTML = '<div class="error">Błąd ładowania grup</div>';
    }
}

async function loadJoinedGroups() {
    const container = document.getElementById('joinedGroups');
    container.innerHTML = '<div class="loading"><div class="spinner"></div>Ładowanie grup...</div>';

    try {
        const groups = await simulateApiCall('/groups/joined');
        renderGroups(groups, container, false);
    } catch (error) {
        container.innerHTML = '<div class="error">Błąd ładowania grup</div>';
    }
}

async function loadDiscoverContent() {
    loadAllMeetups();
}

async function loadAllMeetups() {
    const container = document.getElementById('discoverMeetups');
    container.innerHTML = '<div class="loading"><div class="spinner"></div>Ładowanie spotkań...</div>';

    try {
        const meetups = await simulateApiCall('/meetups');
        renderMeetups(meetups, container, false, true);
    } catch (error) {
        container.innerHTML = '<div class="error">Błąd ładowania spotkań</div>';
    }
}

async function loadAllGroups() {
    const container = document.getElementById('discoverGroups');
    container.innerHTML = '<div class="loading"><div class="spinner"></div>Ładowanie grup...</div>';

    try {
        const groups = await simulateApiCall('/groups');
        renderGroups(groups, container, false, true);
    } catch (error) {
        container.innerHTML = '<div class="error">Błąd ładowania grup</div>';
    }
}

// Funkcje renderowania
function renderMeetups(meetups, container, isOwner = false, showJoinButton = false) {
    if (!meetups || meetups.length === 0) {
        container.innerHTML = '<p style="text-align: center; color: #666; padding: 40px;">Brak spotkań do wyświetlenia</p>';
        return;
    }

    const html = meetups.map(meetup => `
        <div class="card">
            <div class="card-title">${meetup.title}</div>
            <div class="card-meta">
                📅 ${new Date(meetup.dateTime).toLocaleDateString('pl-PL')} o ${new Date(meetup.dateTime).toLocaleTimeString('pl-PL', {hour: '2-digit', minute: '2-digit'})}
                <br>📍 ${meetup.location}
                <br>👥 ${meetup.attendees || 0} uczestników
            </div>
            <p>${meetup.description}</p>
            <div class="card-actions">
                ${isOwner ? `
                    <button class="btn btn-secondary" onclick="editMeetup(${meetup.id})">Edytuj</button>
                    <button class="btn btn-danger" onclick="deleteMeetup(${meetup.id})">Usuń</button>
                ` : ''}
                ${showJoinButton ? `
                    <button class="btn btn-primary" onclick="joinMeetup(${meetup.id})">Dołącz</button>
                ` : ''}
                ${!isOwner && !showJoinButton ? `
                    <button class="btn btn-danger" onclick="leaveMeetup(${meetup.id})">Opuść</button>
                ` : ''}
            </div>
        </div>
    `).join('');

    container.innerHTML = `<div class="grid">${html}</div>`;
}

function renderGroups(groups, container, isOwner = false, showJoinButton = false) {
    if (!groups || groups.length === 0) {
        container.innerHTML = '<p style="text-align: center; color: #666; padding: 40px;">Brak grup do wyświetlenia</p>';
        return;
    }

    const html = groups.map(group => `
        <div class="card">
            <div class="card-title">${group.name}</div>
            <div class="card-meta">
                👥 ${group.members || 0} członków
                ${group.category ? `<br>🏷️ ${group.category}` : ''}
            </div>
            <p>${group.description}</p>
            <div class="card-actions">
                ${isOwner ? `
                    <button class="btn btn-secondary" onclick="editGroup(${group.id})">Edytuj</button>
                    <button class="btn btn-danger" onclick="deleteGroup(${group.id})">Usuń</button>
                    <button class="btn btn-primary" onclick="viewMembers(${group.id})">Członkowie</button>
                ` : ''}
                ${showJoinButton ? `
                    <button class="btn btn-primary" onclick="joinGroup(${group.id})">Dołącz</button>
                ` : ''}
                ${!isOwner && !showJoinButton ? `
                    <button class="btn btn-danger" onclick="leaveGroup(${group.id})">Opuść</button>
                ` : ''}
            </div>
        </div>
    `).join('');

    container.innerHTML = `<div class="grid">${html}</div>`;
}

// Funkcje obsługi spotkań
async function handleCreateMeetup(e) {
    e.preventDefault();

    const meetupData = {
        title: document.getElementById('meetupTitle').value,
        description: document.getElementById('meetupDescription').value,
        dateTime: document.getElementById('meetupDateTime').value,
        location: document.getElementById('meetupLocation').value
    };

    try {
        showMessage('Tworzenie spotkania...', 'info');
        const response = await simulateApiCall('/meetup', {
            method: 'POST',
            body: meetupData
        });

        if (response.success) {
            showMessage('Spotkanie utworzone pomyślnie!', 'success');
            closeModal('createMeetupModal');
            document.getElementById('createMeetupForm').reset();
            loadOrganizedMeetups();
        } else {
            showMessage('Błąd tworzenia spotkania: ' + response.message, 'error');
        }
    } catch (error) {
        showMessage('Błąd połączenia z serwerem', 'error');
    }
}

async function joinMeetup(meetupId) {
    try {
        showMessage('Dołączanie do spotkania...', 'info');
        const response = await simulateApiCall(`/join/${meetupId}`);

        if (response.success) {
            showMessage('Dołączono do spotkania!', 'success');
            loadAllMeetups();
        } else {
            showMessage('Błąd dołączania: ' + response.message, 'error');
        }
    } catch (error) {
        showMessage('Błąd połączenia z serwerem', 'error');
    }
}

async function leaveMeetup(meetupId) {
    if (!confirm('Czy na pewno chcesz opuścić to spotkanie?')) return;

    try {
        showMessage('Opuszczanie spotkania...', 'info');
        const response = await simulateApiCall(`/leave/${meetupId}`);

        if (response.success) {
            showMessage('Opuściłeś spotkanie', 'success');
            loadJoinedMeetups();
        } else {
            showMessage('Błąd: ' + response.message, 'error');
        }
    } catch (error) {
        showMessage('Błąd połączenia z serwerem', 'error');
    }
}

async function deleteMeetup(meetupId) {
    if (!confirm('Czy na pewno chcesz usunąć to spotkanie?')) return;

    try {
        showMessage('Usuwanie spotkania...', 'info');
        const response = await simulateApiCall(`/meetup/${meetupId}`, {
            method: 'DELETE'
        });

        if (response.success) {
            showMessage('Spotkanie usunięte', 'success');
            loadOrganizedMeetups();
        } else {
            showMessage('Błąd usuwania: ' + response.message, 'error');
        }
    } catch (error) {
        showMessage('Błąd połączenia z serwerem', 'error');
    }
}

// Funkcje obsługi grup
async function handleCreateGroup(e) {
    e.preventDefault();

    const groupData = {
        name: document.getElementById('groupName').value,
        description: document.getElementById('groupDescription').value,
        category: document.getElementById('groupCategory').value
    };

    try {
        showMessage('Tworzenie grupy...', 'info');
        const response = await simulateApiCall('/group', {
            method: 'POST',
            body: groupData
        });

        if (response.success) {
            showMessage('Grupa utworzona pomyślnie!', 'success');
            closeModal('createGroupModal');
            document.getElementById('createGroupForm').reset();
            loadOwnedGroups();
        } else {
            showMessage('Błąd tworzenia grupy: ' + response.message, 'error');
        }
    } catch (error) {
        showMessage('Błąd połączenia z serwerem', 'error');
    }
}

async function joinGroup(groupId) {
    try {
        showMessage('Dołączanie do grupy...', 'info');
        const response = await simulateApiCall(`/group/${groupId}/join`);

        if (response.success) {
            showMessage('Dołączono do grupy!', 'success');
            loadAllGroups();
        } else {
            showMessage('Błąd dołączania: ' + response.message, 'error');
        }
    } catch (error) {
        showMessage('Błąd połączenia z serwerem', 'error');
    }
}

async function leaveGroup(groupId) {
    if (!confirm('Czy na pewno chcesz opuścić tę grupę?')) return;

    try {
        showMessage('Opuszczanie grupy...', 'info');
        const response = await simulateApiCall(`/group/${groupId}/leave`);

        if (response.success) {
            showMessage('Opuściłeś grupę', 'success');
            loadJoinedGroups();
        } else {
            showMessage('Błąd: ' + response.message, 'error');
        }
    } catch (error) {
        showMessage('Błąd połączenia z serwerem', 'error');
    }
}

async function deleteGroup(groupId) {
    if (!confirm('Czy na pewno chcesz usunąć tę grupę?')) return;

    try {
        showMessage('Usuwanie grupy...', 'info');
        const response = await simulateApiCall(`/group/${groupId}`, {
            method: 'DELETE'
        });

        if (response.success) {
            showMessage('Grupa usunięta', 'success');
            loadOwnedGroups();
        } else {
            showMessage('Błąd usuwania: ' + response.message, 'error');
        }
    } catch (error) {
        showMessage('Błąd połączenia z serwerem', 'error');
    }
}

// Funkcje pomocnicze
function editMeetup(meetupId) {
    showMessage('Funkcja edycji spotkania będzie wkrótce dostępna', 'info');
}

function editGroup(groupId) {
    showMessage('Funkcja edycji grupy będzie wkrótce dostępna', 'info');
}

function viewMembers(groupId) {
    showMessage('Funkcja przeglądania członków będzie wkrótce dostępna', 'info');
}

function checkAuthStatus() {
    // Sprawdź czy użytkownik jest zalogowany (np. z localStorage w prawdziwej aplikacji)
    const savedUser = localStorage?.getItem('currentUser');
    if (savedUser) {
        try {
            currentUser = JSON.parse(savedUser);
            authToken = localStorage.getItem('authToken');
            showApp();
        } catch (e) {
            showAuth();
        }
    } else {
        showAuth();
    }
}

function showMessage(message, type = 'info') {
    // Usuń poprzednie wiadomości
    const existingMessages = document.querySelectorAll('.message');
    existingMessages.forEach(msg => msg.remove());

    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${type}`;
    messageDiv.textContent = message;

    // Dodaj style dla różnych typów wiadomości
    if (type === 'error') {
        messageDiv.className += ' error';
    } else if (type === 'success') {
        messageDiv.className += ' success';
    }

    // Wstaw wiadomość na górę głównego kontenera
    const mainContent = document.querySelector('.main-content');
    mainContent.insertBefore(messageDiv, mainContent.firstChild);

    // Usuń wiadomość po 5 sekundach
    setTimeout(() => {
        if (messageDiv.parentNode) {
            messageDiv.parentNode.removeChild(messageDiv);
        }
    }, 5000);
}

// Symulacja wywołań API (zastąp prawdziwymi wywołaniami)
async function simulateApiCall(endpoint, options = {}) {
    // Symulacja opóźnienia sieciowego
    await new Promise(resolve => setTimeout(resolve, 500 + Math.random() * 1000));

    // Symulowane dane
    const mockData = {
        '/login': {
            success: true,
            token: 'mock-jwt-token-' + Date.now(),
            user: { id: 1, username: 'testuser', email: 'test@example.com' }
        },
        '/register': {
            success: true,
            message: 'Użytkownik zarejestrowany pomyślnie'
        },
        '/meetups/organized': [
            {
                id: 1,
                title: 'Warsztat JavaScript',
                description: 'Nauka podstaw JavaScript dla początkujących',
                dateTime: '2025-06-15T18:00:00',
                location: 'Centrum Technologiczne, ul. Nowogrodzka 31',
                attendees: 12
            },
            {
                id: 2,
                title: 'Meetup React Developers',
                description: 'Dyskusja o najnowszych trendach w React',
                dateTime: '2025-06-20T19:00:00',
                location: 'TechHub Warsaw, ul. Emilii Plater 53',
                attendees: 8
            }
        ],
        '/meetups/joined': [
            {
                id: 3,
                title: 'Python dla każdego',
                description: 'Wprowadzenie do programowania w Python',
                dateTime: '2025-06-18T17:30:00',
                location: 'Biblioteka Publiczna, ul. Koszykowa 26',
                attendees: 15
            }
        ],
        '/groups/owned': [
            {
                id: 1,
                name: 'Warsaw Tech Enthusiasts',
                description: 'Grupa dla osób zainteresowanych nowymi technologiami',
                category: 'Technologia',
                members: 45
            }
        ],
        '/groups/joined': [
            {
                id: 2,
                name: 'Fotografowie Warszawy',
                description: 'Miejsce dla miłośników fotografii',
                category: 'Hobby',
                members: 32
            }
        ],
        '/meetups': [
            {
                id: 4,
                title: 'Startup Weekend Warsaw',
                description: '54 godziny intensywnej pracy nad startuem',
                dateTime: '2025-06-25T18:00:00',
                location: 'Google Campus Warsaw, ul. Słomińskiego 19',
                attendees: 67
            },
            {
                id: 5,
                title: 'Design Thinking Workshop',
                description: 'Praktyczne podejście do rozwiązywania problemów',
                dateTime: '2025-06-22T10:00:00',
                location: 'Impact Hub Warsaw, ul. Nowogrodzka 56',
                attendees: 24
            }
        ],
        '/groups': [
            {
                id: 3,
                name: 'AI & Machine Learning Warsaw',
                description: 'Społeczność skupiona wokół sztucznej inteligencji',
                category: 'Technologia',
                members: 156
            },
            {
                id: 4,
                name: 'Biegacze Warszawa',
                description: 'Grupa dla miłośników biegania w stolicy',
                category: 'Sport',
                members: 89
            }
        ]
    };

    // Symulacja różnych odpowiedzi w zależności od metody
    if (options.method === 'POST' && endpoint.includes('/meetup')) {
        return { success: true, message: 'Spotkanie utworzone pomyślnie' };
    }

    if (options.method === 'POST' && endpoint.includes('/group')) {
        return { success: true, message: 'Grupa utworzona pomyślnie' };
    }

    if (options.method === 'DELETE') {
        return { success: true, message: 'Usunięto pomyślnie' };
    }

    if (endpoint.includes('/join/') || endpoint.includes('/leave/')) {
        return { success: true, message: 'Operacja wykonana pomyślnie' };
    }

    return mockData[endpoint] || { success: false, message: 'Nieznany endpoint' };
}

// Dodatkowe funkcje API (do implementacji z prawdziwym backend)
async function apiCall(endpoint, options = {}) {
    const url = API_BASE + endpoint;
    const config = {
        method: options.method || 'GET',
        headers: {
            'Content-Type': 'application/json',
            ...(authToken && { 'Authorization': `Bearer ${authToken}` }),
            ...options.headers
        }
    };

    if (options.body) {
        config.body = JSON.stringify(options.body);
    }

    try {
        const response = await fetch(url, config);
        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.message || 'Błąd API');
        }

        return data;
    } catch (error) {
        console.error('API Error:', error);
        throw error;
    }
}

// Przykłady prawdziwych wywołań API (zakomentowane)
/*
async function realApiExamples() {
    // Logowanie
    const loginResponse = await apiCall('/login', {
        method: 'POST',
        body: { username: 'user', password: 'pass' }
    });

    // Pobieranie spotkań użytkownika
    const userMeetups = await apiCall('/meetups/organized');

    // Tworzenie nowego spotkania
    const newMeetup = await apiCall('/meetup', {
        method: 'POST',
        body: {
            title: 'Nowe spotkanie',
            description: 'Opis spotkania',
            dateTime: '2025-06-15T18:00:00',
            locationId: 1
        }
    });

    // Dołączanie do spotkania
    const joinResponse = await apiCall(`/join/${meetupId}`);

    // Pobieranie wszystkich lokalizacji
    const locations = await apiCall('/locations');
}
*/