$(document).ready(function () {
    let commentCurrentSortField = 'reportedAt';
    let commentCurrentSortOrder = 'desc';
    let commentCurrentPage = 0;
    const commentPageSize = 10;

    function fetchCommentReports(page, sortField, sortOrder) {
        $.ajax({
            url: /*[[@{/reviewcommentreport/list}]]*/ '/reviewcommentreport/list',
            data: { page: page, sortField: sortField, sortOrder: sortOrder },
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                renderCommentReportTable(data.content);
                updateCommentPaginationControls(data.number, data.totalPages);
            },
            error: function (xhr, status, error) {
                console.error('Error fetching comment reports:', error);
                $('#commentReportList').html('<tr><td colspan="5">데이터를 불러오는 중 오류가 발생했습니다.</td></tr>');
            }
        });
    }

    function renderCommentReportTable(reports) {
        let tableBody = $('#commentReportList');
        tableBody.empty();
        if (reports && reports.length > 0) {
            reports.forEach(function (report) {
                let row = createCommentReportRow(report);
                tableBody.append(row);
            });
        } else {
            tableBody.append('<tr><td colspan="5">신고된 댓글이 없습니다.</td></tr>');
        }
    }

    function createCommentReportRow(report) {
        let formattedDate = new Date(report.reportedAt).toLocaleDateString();
        let statusClass = report.checked ? 'report-completed' : '';
        let statusText = report.checked ? '처리 완료' : '처리 전';
        return `
            <tr data-id="${report.id}">
                <td>${report.id}</td>
                <td>${report.title}</td>
                <td>${report.reviewComment.content}</td>
                <td>${formattedDate}</td>
                <td class="report-status ${statusClass}" aria-label="처리 상태">${statusText}</td>
            </tr>
        `;
    }

    function updateCommentPaginationControls(currentPage, totalPages) {
        let paginationElement = $('#commentPagination');
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
                commentCurrentPage = page;
                fetchCommentReports(commentCurrentPage, commentCurrentSortField, commentCurrentSortOrder);
            }
        });
    }

    // 정렬 버튼 이벤트 리스너
    $('.sort-btn').click(function () {
        let newSortField = $(this).data('sort');
        if (newSortField === commentCurrentSortField) {
            commentCurrentSortOrder = commentCurrentSortOrder === 'asc' ? 'desc' : 'asc';
        } else {
            commentCurrentSortField = newSortField;
            commentCurrentSortOrder = 'desc';
        }
        updateSortButtonsUI();
        fetchCommentReports(commentCurrentPage, commentCurrentSortField, commentCurrentSortOrder);
    });

    function updateSortButtonsUI() {
        $('.sort-btn').removeClass('active');
        $(`.sort-btn[data-sort="${commentCurrentSortField}"]`).addClass('active');
        $('.sort-icon').removeClass('fa-sort-up fa-sort-down').addClass('fa-sort');
        $(`.sort-btn[data-sort="${commentCurrentSortField}"] .sort-icon`)
            .removeClass('fa-sort')
            .addClass(commentCurrentSortOrder === 'asc' ? 'fa-sort-up' : 'fa-sort-down');
    }

    // 초기 댓글 신고 목록 로드
    $('#comment-tab').on('shown.bs.tab', function (e) {
        fetchCommentReports(commentCurrentPage, commentCurrentSortField, commentCurrentSortOrder);
    });

    function loadCommentReportDetail(reportId) {
        $.ajax({
            url: `/reviewcommentreport/detail/${reportId}`,
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                let report = data.report;
                let detailHtml = `
                    <h6>신고 번호: <span id="commentReportId">${report.id}</span></h6>
                    <p>신고 제목: <span id="commentReportTitle">${report.title}</span></p>
                    <p>신고 내용: <span id="commentReportContent">${report.content}</span></p>
                    <p>댓글 내용: <span id="commentContent">${report.reviewComment.content}</span></p>
                    <p>신고일: <span id="commentReportedAt">${new Date(report.reportedAt).toLocaleDateString()}</span></p>
                    <p>처리여부: <span id="commentReportChecked">${report.checked ? '처리완료' : '처리 전'}</span></p>
                    <button type="button" class="btn btn-danger" id="commentPunish">처벌</button>
                    <button type="button" class="btn btn-warning" id="commentPass">보류</button>
                `;
                $('#commentReportDetail').html(detailHtml);
            },
            error: function (xhr, status, error) {
                console.error('Error loading comment report detail:', error);
                $('#commentReportDetail').html('<p>데이터를 불러오는 중 오류가 발생했습니다.</p>');
            }
        });
    }

    function updateCommentReport(reportId, btnId) {
        if (confirm("댓글 신고처리를 진행하시겠습니까?")) {
            $.ajax({
                url: `/reviewcommentreport/update/${reportId}`,
                type: "PUT",
                data: { btnId: btnId },
                success: function () {
                    alert('댓글 신고처리가 완료되었습니다.');
                    fetchCommentReports(commentCurrentPage, commentCurrentSortField, commentCurrentSortOrder);
                    loadCommentReportDetail(reportId);
                    // UI 업데이트
                    $(`tr[data-id="${reportId}"] .report-status`)
                        .addClass('report-completed')
                        .text('처리 완료')
                        .attr('aria-label', '처리 상태: 처리 완료');
                },
                error: function (xhr, status, error) {
                    console.error('Error updating comment report:', error);
                    alert('댓글 신고처리에 실패했습니다.');
                }
            });
        }
    }

    // 이벤트 리스너들
    $(document).on('click', '#commentReportList tr', function () {
        let reportId = $(this).data('id');
        loadCommentReportDetail(reportId);
    });

    $(document).on('click', '#commentPunish, #commentPass', function () {
        let btnId = $(this).attr('id');
        let reportId = $('#commentReportId').text();
        updateCommentReport(reportId, btnId);
    });
});
