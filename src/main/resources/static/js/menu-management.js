document.addEventListener('DOMContentLoaded', function () {
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
    async function loadMenus() {
        // TODO: 백엔드 API 호출로 실제 메뉴 데이터 가져오기
        return new Promise((resolve, reject) => {
            fetch("/api/restaurant/menus")
                .then((response) => response.json())
                .then((data) => {
                    resolve(data);
                })
                .catch((error) => console.error("음식점 메뉴 정보 로드 실패:", error));
        });
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
    async function updateMenuList() {

        const menus = await loadMenus();
        const start = (currentPage - 1) * itemsPerPage;

        // start부터 itemsPerPage만큼의 메뉴를 가져옴
        // 새로운 배열을 생성하여 전달하기
        let paginatedMenus = [];
        for (let i = start; i < start + itemsPerPage; i++) {
            if (menus[i] !== undefined) {
                menus[i].image = menus[i].image != null ? menus[i].image : 'https://via.placeholder.com/150';
                paginatedMenus.push(menus[i]);
            }
        }

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
        var menu;
        // TODO: 백엔드에서 해당 id의 메뉴 정보 가져오기
        $.ajax({
            type: "GET",
            url: "/api/restaurant/menu/" + id,
            success: function (response) {
                document.getElementById('menuId').value = response.id;
                document.getElementById('menuName').value = response.name;
                document.getElementById('menuPrice').value = response.price;
                // TODO: 메뉴 이미지 수정 추가
                document.getElementById('menuModalLabel').textContent = '메뉴 수정';
                menuModal.show();
            },
            error: function (error) {
                console.log("error : ", error);
            }
        })
    }

    // 메뉴 삭제
    function deleteMenu(id) {
        if (confirm('정말로 이 메뉴를 삭제하시겠습니까?')) {
            // TODO: 백엔드 API 호출하여 메뉴 삭제
            $.ajax({
                type: "DELETE",
                url: "/api/restaurant/deleteMenu/" + id,
                success: function (response) {
                    updateMenuList();
                },
                error: function (error) {
                    console.log("error : ", error);
                }
            });
        }
    }

    // 메뉴 저장 (추가 또는 수정)
    saveMenuBtn.addEventListener('click', () => {
        const menuData = {
            id: document.getElementById('menuId').value,
            name: document.getElementById('menuName').value,
            price: document.getElementById('menuPrice').value,
            restaurantId: document.getElementById('restaurantId').value
            // TODO: 이미지 처리 로직 추가
        };

        // TODO: 백엔드 API 호출하여 메뉴 저장
        $.ajax({
            type: "POST",
            url: "/api/restaurant/saveMenu",
            data: menuData,
            success: function (response) {
                alert("메뉴 저장에 성공했습니다.");
                console.log("response : ", response);
            },
            error: function (error) {
                alert("메뉴 저장에 실패했습니다.");
                console.log("error : ", error);
            }
        });
        menuModal.hide();
        updateMenuList();
    });

    // 초기 메뉴 리스트 로드
    updateMenuList();
}

