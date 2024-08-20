document.addEventListener('DOMContentLoaded', function() {
    initializeMenuManagement();
});

function initializeMenuManagement() {
    const menuList = document.getElementById('menuList');
    const menuPagination = document.getElementById('menuPagination');
    const addNewMenuBtn = document.getElementById('addNewMenuBtn');
    const menuModal = new bootstrap.Modal(document.getElementById('menuModal'));
    const menuForm = document.getElementById('menuForm');
    const saveMenuBtn = document.getElementById('saveMenuBtn');

    let currentPage = 1;
    const itemsPerPage = 8;

    // 메뉴 데이터 로드 (백엔드 연동 전 더미 데이터 사용)
    function loadMenus() {
        // TODO: 백엔드 API 호출로 실제 메뉴 데이터 가져오기
        const dummyMenus = [
            { id: 1, name: '민트초코 불고기', description: '상쾌한 민트와 달콤한 초코의 조화', price: 15000, image: 'https://via.placeholder.com/150' },
            { id: 2, name: '와사비 아이스크림 비빔밥', description: '매콤하고 차가운 비빔밥', price: 12000, image: 'https://via.placeholder.com/150' },
            { id: 3, name: '콜라 김치찌개', description: '톡톡 튀는 탄산 김치찌개', price: 10000, image: 'https://via.placeholder.com/150' },
            { id: 4, name: '초콜릿 삼겹살', description: '달콤한 초콜릿 코팅 삼겹살', price: 18000, image: 'https://via.placeholder.com/150' },
            { id: 5, name: '블루베리 된장찌개', description: '새콤달콤한 블루베리 된장찌개', price: 9000, image: 'https://via.placeholder.com/150' },
            { id: 6, name: '바나나 갈비탕', description: '이국적인 바나나 향의 갈비탕', price: 14000, image: 'https://via.placeholder.com/150' },
            { id: 7, name: '치약맛 떡볶이', description: '상쾌한 치약 향의 떡볶이', price: 8000, image: 'https://via.placeholder.com/150' },
            { id: 8, name: '커피 냉면', description: '카페인 충전 시원한 냉면', price: 11000, image: 'https://via.placeholder.com/150' },
            { id: 9, name: '두리안 순대국', description: '독특한 향의 순대국', price: 10000, image: 'https://via.placeholder.com/150' },
            { id: 10, name: '마늘 아이스크림 제육볶음', description: '달콤하고 알싸한 제육볶음', price: 13000, image: 'https://via.placeholder.com/150' },
            { id: 11, name: '피자맛 김밥', description: '이탈리안 스타일 김밥', price: 5000, image: 'https://via.placeholder.com/150' },
            { id: 12, name: '김치 파스타', description: '한국식 퓨전 파스타', price: 16000, image: 'https://via.placeholder.com/150' }
        ];
        return dummyMenus;
    }

    // 메뉴 리스트 렌더링
    function renderMenus(menus) {
        menuList.innerHTML = '';
        menus.forEach(menu => {
            const menuItem = document.createElement('div');
            menuItem.className = 'col-md-3 mb-4';
            menuItem.innerHTML = `
                <div class="card">
                    <img src="${menu.image}" class="card-img-top" alt="${menu.name}">
                    <div class="card-body">
                        <h5 class="card-title">${menu.name}</h5>
                        <p class="card-text">${menu.description}</p>
                        <p class="card-text"><strong>${menu.price.toLocaleString()}원</strong></p>
                        <button class="btn btn-sm btn-primary edit-menu" data-id="${menu.id}">수정</button>
                        <button class="btn btn-sm btn-danger delete-menu" data-id="${menu.id}">삭제</button>
                    </div>
                </div>
            `;
            menuList.appendChild(menuItem);
        });

        // 수정 및 삭제 버튼에 이벤트 리스너 추가
        document.querySelectorAll('.edit-menu').forEach(btn => {
            btn.addEventListener('click', (e) => editMenu(e.target.dataset.id));
        });
        document.querySelectorAll('.delete-menu').forEach(btn => {
            btn.addEventListener('click', (e) => deleteMenu(e.target.dataset.id));
        });
    }

    // 페이지네이션 렌더링
    function renderPagination(totalItems) {
        const pageCount = Math.ceil(totalItems / itemsPerPage);
        menuPagination.innerHTML = '';
        for (let i = 1; i <= pageCount; i++) {
            const li = document.createElement('li');
            li.className = `page-item ${i === currentPage ? 'active' : ''}`;
            li.innerHTML = `<a class="page-link" href="#" data-page="${i}">${i}</a>`;
            menuPagination.appendChild(li);
        }

        // 페이지네이션 버튼에 이벤트 리스너 추가
        menuPagination.querySelectorAll('.page-link').forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                currentPage = parseInt(e.target.dataset.page);
                updateMenuList();
            });
        });
    }

    // 메뉴 리스트 업데이트
    function updateMenuList() {
        const menus = loadMenus();
        const start = (currentPage - 1) * itemsPerPage;
        const paginatedMenus = menus.slice(start, start + itemsPerPage);
        renderMenus(paginatedMenus);
        renderPagination(menus.length);
    }

    // 새 메뉴 추가
    addNewMenuBtn.addEventListener('click', () => {
        menuForm.reset();
        document.getElementById('menuId').value = '';
        document.getElementById('menuModalLabel').textContent = '새 메뉴 추가';
        menuModal.show();
    });

    // 메뉴 수정
    function editMenu(id) {
        // TODO: 백엔드에서 해당 id의 메뉴 정보 가져오기
        const menu = loadMenus().find(m => m.id === parseInt(id));
        if (menu) {
            document.getElementById('menuId').value = menu.id;
            document.getElementById('menuName').value = menu.name;
            document.getElementById('menuDescription').value = menu.description;
            document.getElementById('menuPrice').value = menu.price;
            document.getElementById('menuModalLabel').textContent = '메뉴 수정';
            menuModal.show();
        }
    }

    // 메뉴 삭제
    function deleteMenu(id) {
        if (confirm('정말로 이 메뉴를 삭제하시겠습니까?')) {
            // TODO: 백엔드 API 호출하여 메뉴 삭제
            console.log(`메뉴 ID ${id} 삭제`);
            updateMenuList();
        }
    }

    // 메뉴 저장 (추가 또는 수정)
    saveMenuBtn.addEventListener('click', () => {
        const menuData = {
            id: document.getElementById('menuId').value,
            name: document.getElementById('menuName').value,
            description: document.getElementById('menuDescription').value,
            price: parseInt(document.getElementById('menuPrice').value),
            // TODO: 이미지 처리 로직 추가
        };

        // TODO: 백엔드 API 호출하여 메뉴 저장
        console.log('메뉴 저장:', menuData);
        menuModal.hide();
        updateMenuList();
    });

    // 초기 메뉴 리스트 로드
    updateMenuList();
}
