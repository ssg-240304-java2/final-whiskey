// 현재 페이지와 정렬 순서를 관리하는 변수
let currentPage = 0;
let currentSortField = 'date'; // 기본 정렬 필드
let currentSortDirection = 'desc'; // 기본 정렬 방향

// 페이지 로드 시 실행되는 함수
$(document).ready(function() {
    loadRestaurantRegistrations();
    initializeSortButtons();

    $('.btn-secondary[data-bs-dismiss="modal"], .close[data-bs-dismiss="modal"]').click(function() {
        closeModal();
    });
});

// 정렬 버튼 초기화 함수
function initializeSortButtons() {
    $('.sort-btn').click(function() {
        const sortField = $(this).data('sort');
        if (sortField === currentSortField) {
            currentSortDirection = currentSortDirection === 'asc' ? 'desc' : 'asc';
        } else {
            currentSortField = sortField;
            currentSortDirection = 'asc';
        }
        updateSortButtonsUI();
        loadRestaurantRegistrations();
    });
}

// 정렬 버튼 UI 업데이트 함수
function updateSortButtonsUI() {
    $('.sort-btn').removeClass('active');
    $('.sort-btn[data-sort="' + currentSortField + '"]').addClass('active');
    $('.sort-icon').removeClass('fa-sort-up fa-sort-down').addClass('fa-sort');
    $('.sort-btn[data-sort="' + currentSortField + '"] .sort-icon')
        .removeClass('fa-sort')
        .addClass(currentSortDirection === 'asc' ? 'fa-sort-up' : 'fa-sort-down');
}

// 입점 신청 데이터를 가져오는 함수
function loadRestaurantRegistrations() {
    $.ajax({
        url: `/businessregister/list?page=${currentPage}&sortField=${currentSortField}&sortDirection=${currentSortDirection}`,
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
                <td>${formatDate(restaurant.createdAt)}</td>
                <td><span class="report-status ${getStatusClass(restaurant.registrationStatus)}">${restaurant.registrationStatus}</span></td>
                <td>
                    <button class="btn btn-sm btn-primary" onclick="viewDetails(${restaurant.id})">상세보기</button>
                </td>
            </tr>
        `;
        tableBody.append(row);
    });
}

// 날짜 포맷 함수
function formatDate(dateString) {
    const options = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' };
    return new Date(dateString).toLocaleDateString('ko-KR', options);
}

// TODO: 백엔드에서 정렬된 데이터를 받아오는 API 구현 필요
// TODO: 승인 및 거절 처리 후 목록 새로고침 로직 구현 필요

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

// 상세보기 기능 구현
function viewDetails(id) {
    $.ajax({
        url: `/businessregister/detail/${id}`,
        type: "GET",
        dataType: "json",
        success: function(data) {
            const register = data.register;
            $('#modalApplyId').text(register.id);
            $('#modalRestaurantTitle').text(register.restaurantName);
            $('#modalCategory').text(register.restaurantCategory);
            $('#modalAddress').text(register.restaurantAddress.name);
            $('#modalPhone').text(register.restaurantNumber);
            $('#modalApplier').text(register.member.name);
            $('#modalCreatedAt').text(new Date(register.createdAt).toLocaleDateString());
            $('#modalStatus').text(register.registrationStatus);
            
            // 모달 버튼에 ID 설정
            $('#modalApprove').attr('onclick', `approveRegistration(${register.id})`);
            $('#modalReject').attr('onclick', `rejectRegistration(${register.id})`);
            
            $('#restaurantDetailModal').modal('show');
        },
        error: function(xhr, status, error) {
            console.error('상세 정보를 불러오는 데 실패했습니다:', error);
            alert('상세 정보를 불러오는 데 실패했습니다.');
        }
    });
}


// 승인 기능 구현
function approveRegistration(id) {
    console.log('승인:', id);
    // 여기에 승인 처리 로직을 구현합니다.
    const btnID = 'approve';
    updateReport(btnID,id)
}

// 거절 기능 구현
function rejectRegistration(id) {
    console.log('거절:', id);
    // 여기에 거절 처리 로직을 구현합니다.
    const btnID = 'reject';
    updateReport(btnID,id)
}

// 모달 닫기 기능 구현
function closeModal() {
    $("#restaurantDetailModal").modal("hide");
}

// 입점 처리 기능
function updateReport(btnId, id) {
    if (confirm("입점처리를 진행하시겠습니까?")) {
        $.ajax({
            url: `/businessregister/process/${id}`,
            type: "PUT",
            data: {btnId: btnId},
            success: function() {
                alert('입점처리가 완료되었습니다.');
                $('#restaurantDetailModal').modal('hide');
                loadRestaurantRegistrations(); // 목록 새로고침
            },
            error: function() {
                alert('입점처리에 실패했습니다.');
            }
        });
    }
}
