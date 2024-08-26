
$(document).ready(function () {
    let reviewCurrentSortField = 'reportedAt';
    let reviewCurrentSortOrder = 'desc';
    let reviewCurrentPage = 0;
    const reviewPageSize = 10;

    function fetchReviewReports(page, sortField, sortOrder) {
        $.ajax({
            url: /*[[@{/reviewreport/list}]]*/ '/reviewreport/list',
            data: { page: page, sortField: sortField, sortOrder: sortOrder },
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                renderReviewReportTable(data.content);
                updateReviewPaginationControls(data.number, data.totalPages);
            },
            error: function (xhr, status, error) {
                console.error('Error fetching review reports:', error);
                $('#reviewReportList').html('<tr><td colspan="6">데이터를 불러오는 중 오류가 발생했습니다.</td></tr>');
            }
        });
    }

    function renderReviewReportTable(reports) {
        let tableBody = $('#reviewReportList');
        tableBody.empty();
        if (reports && reports.length > 0) {
            reports.forEach(function (report) {
                let row = createReviewReportRow(report);
                tableBody.append(row);
            });
        } else {
            tableBody.append('<tr><td colspan="6">신고된 리뷰가 없습니다.</td></tr>');
        }
    }

    function createReviewReportRow(report) {
        let formattedDate = new Date(report.reportedAt).toLocaleDateString();
        let statusClass = report.checked ? 'report-completed' : '';
        let statusText = report.checked ? '처리 완료' : '처리 전';
        return `
            <tr data-id="${report.id}">
                <td>${report.id}</td>
                <td>${report.title}</td>
                <td>${report.content}</td>
                <td>${report.review.content}</td>
                <td>${formattedDate}</td>
                <td class="report-status ${statusClass}" aria-label="처리 상태">${statusText}</td>
            </tr>
        `;
    }

    function updateReviewPaginationControls(currentPage, totalPages) {
        let paginationElement = $('#reviewPagination');
        paginationElement.empty();

        // 이전 페이지 버튼
        paginationElement.append(`
            <li class="page-item ${currentPage === 0 ? 'disabled' : ''}">
                <a class="page-link" href="#" data-page="${currentPage - 1}" aria-label="Previous">
                    <span aria-hidden="true">«</span>
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
                    <span aria-hidden="true">»</span>
                </a>
            </li>
        `);

        // 페이지 클릭 이벤트 핸들러
        $('.page-link').click(function(e) {
            e.preventDefault();
            let page = $(this).data('page');
            if (page >= 0 && page < totalPages) {
                reviewCurrentPage = page;
                fetchReviewReports(reviewCurrentPage, reviewCurrentSortField, reviewCurrentSortOrder);
            }
        });
    }

    // 정렬 버튼 이벤트 리스너
    $('.sort-btn').click(function () {
        let newSortField = $(this).data('sort');
        if (newSortField === reviewCurrentSortField) {
            reviewCurrentSortOrder = reviewCurrentSortOrder === 'asc' ? 'desc' : 'asc';
        } else {
            reviewCurrentSortField = newSortField;
            reviewCurrentSortOrder = 'desc';
        }
        updateSortButtonsUI();
        fetchReviewReports(reviewCurrentPage, reviewCurrentSortField, reviewCurrentSortOrder);
    });

    function updateSortButtonsUI() {
        $('.sort-btn').removeClass('active');
        $(`.sort-btn[data-sort="${reviewCurrentSortField}"]`).addClass('active');
        $('.sort-icon').removeClass('fa-sort-up fa-sort-down').addClass('fa-sort');
        $(`.sort-btn[data-sort="${reviewCurrentSortField}"] .sort-icon`)
            .removeClass('fa-sort')
            .addClass(reviewCurrentSortOrder === 'asc' ? 'fa-sort-up' : 'fa-sort-down');
    }

    // 초기 리뷰 신고 목록 로드
    $('#review-tab').on('shown.bs.tab', function (e) {
        fetchReviewReports(reviewCurrentPage, reviewCurrentSortField, reviewCurrentSortOrder);
    });

    function loadReviewReportDetail(reportId) {
        if (reportId === undefined || reportId === null) {
            console.error('Invalid reportId');
            return;
        }

        $.ajax({
            url: `/reviewreport/detail/${reportId}`,
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                let report = data.report;
                let detailHtml = `
                      <h6>신고 번호: <span id="reviewReportId">${report.id}</span></h6>
                      <p>신고 제목: <span id="reviewReportTitle">${report.title}</span></p>
                      <p>신고 내용: <span id="reviewReportContent">${report.content}</span></p>
                      <p>리뷰 내용: <span id="reviewTitle">${report.review.content}</span></p>
                      <p>신고일: <span id="reviewReportedAt">${new Date(report.reportedAt).toLocaleDateString()}</span></p>
                      <p>처리여부: <span id="reviewReportChecked">${report.checked ? '처리완료' : '처리 전'}</span></p>
                      <button type="button" class="btn btn-danger" id="reviewPunish">처벌</button>
                      <button type="button" class="btn btn-warning" id="reviewPass">보류</button>
                  `;
                $('#reviewReportDetail').html(detailHtml);
            },
            error: function (xhr, status, error) {
                console.error('Error loading report detail:', error);
                $('#reviewReportDetail').html('<p>데이터를 불러오는 중 오류가 발생했습니다.</p>');
            }
        });
    }

    function updateReviewReport(reportId, btnId) {
        if (confirm("리뷰 신고처리를 진행하시겠습니까?")) {
            $.ajax({
                url: `/reviewreport/update/${reportId}`,
                type: "PUT",
                data: { btnId: btnId },
                success: function () {
                    alert('리뷰 신고처리가 완료되었습니다.');
                    fetchReviewReports(reviewCurrentPage, reviewCurrentSortField, reviewCurrentSortOrder);
                    loadReviewReportDetail(reportId);
                    // UI 업데이트
                    $(`tr[data-id="${reportId}"] .report-status`)
                        .addClass('report-completed')
                        .text('처리 완료')
                        .attr('aria-label', '처리 상태: 처리 완료');
                },
                error: function () {
                    alert('리뷰 신고처리에 실패했습니다.');
                }
            });
        }
    }

    // 이벤트 리스너들
    $(document).on('click', '#reviewReportList tr', function () {
        let reportId = $(this).data('id');
        loadReviewReportDetail(reportId);
    });

    $(document).on('click', '#reviewPunish, #reviewPass', function () {
        let btnId = $(this).attr('id');
        let reportId = $('#reviewReportId').text();
        updateReviewReport(reportId, btnId);
    });
});
