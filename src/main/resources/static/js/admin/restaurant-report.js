$(document).ready(function () {
    let currentSortField = 'reportedAt';
    let currentSortOrder = 'desc';
    let currentPage = 0;
    const pageSize = 10;
    let allReports = []; // 모든 리포트를 저장할 배열

    fetchRestaurantReports(currentPage, currentSortField, currentSortOrder);
    initializeSortButtons();

    function fetchRestaurantReports(page, sortField, sortOrder) {
        $.ajax({
            url: /*[[@{/restaurantreport/list}]]*/ '/restaurantreport/list',
            data: { page: page, sortField: sortField, sortOrder: sortOrder },
            type: 'GET',
            success: function (data) {
                allReports = data.content; // 모든 리포트 저장
                renderReportTable(allReports);
                updatePaginationControls(data.number, data.totalPages);
            },
            error: function (xhr, status, error) {
                handleFetchError(error);
            }
        });
    }

    function renderReportTable(reports) {
        let tableBody = $('#restaurantReportList');
        tableBody.empty();
        if (reports && reports.length > 0) {
            reports.forEach(function (report) {
                let row = createReportRow(report);
                tableBody.append(row);
            });
        } else {
            tableBody.append('<tr><td colspan="5">신고된 식당이 없습니다.</td></tr>');
        }
    }

    function createReportRow(report) {
        let formattedDate = new Date(report.reportedAt).toLocaleDateString();
        let statusClass = report.checked ? 'report-completed' : '';
        let statusText = report.checked ? '처리 완료' : '처리 전';
        return `
            <tr data-id="${report.id}">
                <td>${report.id}</td>
                <td>${report.restaurant ? report.restaurant.name : 'N/A'}</td>
                <td>${report.title}</td>
                <td>${formattedDate}</td>
                <td class="report-status ${statusClass}" aria-label="처리 상태">${statusText}</td>
            </tr>
        `;
    }

    function updatePaginationControls(currentPage, totalPages) {
        let paginationElement = $('#pagination');
        paginationElement.empty();

        // 이전 페이지 버튼
        paginationElement.append(`
            <li class="page-item ${currentPage === 0 ? 'disabled' : ''}">
                <a class="page-link" href="#" data-page="${currentPage - 1}" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
        `);

        // 페이지 번호
        let startPage = Math.max(0, currentPage - 2);
        let endPage = Math.min(totalPages - 1, currentPage + 2);

        for (let i = startPage; i <= endPage; i++) {
            paginationElement.append(`
                <li class="page-item ${i === currentPage ? 'active' : ''}">
                    <a class="page-link" href="#" data-page="${i}">${i + 1}</a>
                </li>
            `);
        }

        // 다음 페이지 버튼
        paginationElement.append(`
            <li class="page-item ${currentPage === totalPages - 1 ? 'disabled' : ''}">
                <a class="page-link" href="#" data-page="${currentPage + 1}" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
        `);

        // 페이지 클릭 이벤트 핸들러
        $('.page-link').click(function(e) {
            e.preventDefault();
            let page = $(this).data('page');
            if (page >= 0 && page < totalPages) {
                currentPage = page;
                fetchRestaurantReports(currentPage, currentSortField, currentSortOrder);
            }
        });
    }

    function loadRestaurantReportDetail(reportId) {
        $.ajax({
            url: `/restaurantreport/detail/${reportId}`,
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                renderReportDetail(data.report);
            },
            error: function (xhr, status, error) {
                console.error("Error fetching report detail:", error);
                $('#restaurantReportDetail').html('<p>상세 정보를 불러오는 데 실패했습니다.</p>');
            }
        });
    }

    function renderReportDetail(report) {
        let detailHtml = `
            <h6>신고 번호: <span id="reportId">${report.id}</span></h6>
            <p>식당 이름: <span id="restaurantTitle">${report.restaurant.name}</span></p>
            <p>제목: <span id="title">${report.title}</span></p>
            <p>내용: <span id="content">${report.content}</span></p>
            <p>신고일: <span id="reportedAt">${new Date(report.reportedAt).toLocaleDateString()}</span></p>
            <p>처리여부: <span id="checked">${report.checked ? '처리완료' : '처리 전'}</span></p>
            <button type="button" class="btn btn-danger" id="punish">처벌</button>
            <button type="button" class="btn btn-warning" id="pass">보류</button>
        `;
        $('#restaurantReportDetail').html(detailHtml);
    }

    function updateReport(reportId, btnId) {
        if (confirm("신고처리를 진행하시겠습니까?")) {
            $.ajax({
                url: `/restaurantreport/update/${reportId}`,
                type: "PUT",
                data: { btnId: btnId },
                success: function () {
                    handleReportUpdateSuccess(reportId);
                },
                error: function () {
                    alert('신고처리에 실패했습니다.');
                }
            });
        }
    }

    function handleReportUpdateSuccess(reportId) {
        alert('신고처리 되었습니다.');
        fetchRestaurantReports(currentPage, currentSortField, currentSortOrder);
        loadRestaurantReportDetail(reportId);
        updateReportStatusUI(reportId);
    }

    function updateReportStatusUI(reportId) {
        $(`tr[data-id="${reportId}"] .report-status`)
            .addClass('report-completed')
            .text('처리 완료')
            .attr('aria-label', '처리 상태: 처리 완료');
    }

    function initializeSortButtons() {
        $('.sort-btn').click(function () {
            let newSortField = $(this).data('sort');
            if (newSortField === currentSortField) {
                currentSortOrder = currentSortOrder === 'asc' ? 'desc' : 'asc';
            } else {
                currentSortField = newSortField;
                currentSortOrder = 'desc';
            }
            updateSortButtonsUI();
            sortReports();
        });
    }

    function sortReports() {
        allReports.sort((a, b) => {
            if (currentSortField === 'reportedAt') {
                return currentSortOrder === 'asc' 
                    ? new Date(a.reportedAt) - new Date(b.reportedAt)
                    : new Date(b.reportedAt) - new Date(a.reportedAt);
            } else if (currentSortField === 'status') {
                return currentSortOrder === 'asc'
                    ? (a.checked === b.checked ? 0 : a.checked ? 1 : -1)
                    : (a.checked === b.checked ? 0 : a.checked ? -1 : 1);
            }
        });
        renderReportTable(allReports);
    }

    function updateSortButtonsUI() {
        $('.sort-btn').removeClass('active');
        $(`.sort-btn[data-sort="${currentSortField}"]`).addClass('active');
        $('.sort-icon').removeClass('fa-sort-up fa-sort-down').addClass('fa-sort');
        $(`.sort-btn[data-sort="${currentSortField}"] .sort-icon`)
            .removeClass('fa-sort')
            .addClass(currentSortOrder === 'asc' ? 'fa-sort-up' : 'fa-sort-down');
    }

    function handleFetchError(error) {
        $('#restaurantReportList').html('<tr><td colspan="5">데이터를 불러오는 중 오류가 발생했습니다.</td></tr>');
        console.error("Error fetching restaurant reports:", error);
    }

    // 이벤트 리스너들
    $(document).on('click', '#restaurantReportList tr', function () {
        let reportId = $(this).data('id');
        loadRestaurantReportDetail(reportId);
    });

    $(document).on('click', '#punish, #pass', function () {
        let btnId = $(this).attr('id');
        let reportId = $('#reportId').text();
        updateReport(reportId, btnId);
    });
});
