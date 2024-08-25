// 현재 페이지와 정렬 순서를 관리하는 변수
let currentPage = 0;
let currentSortOrder = 'asc';

// 페이지 로드 시 실행되는 함수
$(document).ready(function() {
    loadRestaurantRegistrations();
});

// 입점 신청 데이터를 가져오는 함수
function loadRestaurantRegistrations() {
    $.ajax({
        url: `/businessregister/list?page=${currentPage}&sortOrder=${currentSortOrder}`,
        method: 'GET',
        success: function(data) {
            renderRestaurantTable(data.content);
            updatePagination(data.number, data.totalPages);
        },
        error: function(xhr, status, error) {
            console.error('입점 신청 데이터를 불러오는 데 실패했습니다:', error);
            $('#restaurantTableBody').html('<tr><td colspan="9" class="text-center">데이터를 불러오는 데 실패했습니다.</td></tr>');
        }
    });
}

// 테이블을 렌더링하는 함수
function renderRestaurantTable(restaurants) {
    const tableBody = $('#restaurantTableBody');
    tableBody.empty();

    restaurants.forEach(restaurant => {
        const row = `
            <tr>
                <td>${restaurant.id}</td>
                <td>${restaurant.restaurantName}</td>
                <td>${restaurant.restaurantCategory}</td>
                <td>${restaurant.member.name}</td>
                <td>${restaurant.restaurantNumber}</td>
                <td>${new Date(restaurant.createdAt).toLocaleDateString()}</td>
                <td><span class="report-status ${getStatusClass(restaurant.registrationStatus)}">${restaurant.registrationStatus}</span></td>
                <td>
                    <button class="btn btn-sm btn-primary" onclick="viewDetails(${restaurant.id})">상세보기</button>
                    <button class="btn btn-sm btn-success" onclick="approveRegistration(${restaurant.id})">승인</button>
                    <button class="btn btn-sm btn-danger" onclick="rejectRegistration(${restaurant.id})">거절</button>
                </td>
            </tr>
        `;
        tableBody.append(row);
    });
}

// 페이지네이션을 업데이트하는 함수
function updatePagination(currentPage, totalPages) {
    const pagination = $('#restaurantPagination');
    pagination.empty();

    for (let i = 0; i < totalPages; i++) {
        const pageItem = `
            <li class="page-item ${i === currentPage ? 'active' : ''}">
                <a class="page-link" href="#" onclick="changePage(${i})">${i + 1}</a>
            </li>
        `;
        pagination.append(pageItem);
    }
}
// 상태에 따른 클래스 반환 함수
function getStatusClass(status) {
    switch(status) {
        case '승인대기':
            return 'pending';
        case '승인완료':
            return 'completed';
        case '거절':
            return 'rejected';
        default:
            return '';
    }
}

// 페이지 변경 함수
function changePage(page) {
    currentPage = page;
    loadRestaurantRegistrations();
}

// TODO: 상세보기 기능 구현
function viewDetails(id) {
    console.log('상세보기:', id);
    // 여기에 상세보기 모달 또는 새 페이지로 이동하는 로직을 구현합니다.
}

// TODO: 승인 기능 구현
function approveRegistration(id) {
    console.log('승인:', id);
    // 여기에 승인 처리 로직을 구현합니다.
}

// TODO: 거절 기능 구현
function rejectRegistration(id) {
    console.log('거절:', id);
    // 여기에 거절 처리 로직을 구현합니다.
}