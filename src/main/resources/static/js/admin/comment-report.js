
$(document).ready(function () {
    let commentCurrentSortOrder = 'asc';
    let commentCurrentPage = 0;
    const commentPageSize = 10;

    function fetchCommentReports(page, sortOrder = 'asc') {
        $.ajax({
            url: /*[[@{/reviewcommentreport/list}]]*/ '/reviewcommentreport/list',
            data: { page: page, sortOrder: sortOrder },
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                let tableBody = $('#commentReportList');
                tableBody.empty();
                if (data && data.content && data.content.length > 0) {
                    data.content.forEach(function (report) {
                        let formattedDate = new Date(report.reportedAt).toLocaleDateString();
                        let row = `
                            <tr data-id="${report.id}">
                                <td>${report.id}</td>
                                <td>${report.title}</td>
                                <td>${report.reviewComment.content}</td>
                                <td>${formattedDate}</td>
                                <td>${report.checked ? '처리 완료' : '처리 전'}</td>
                            </tr>
                        `;
                        tableBody.append(row);
                    });
                    updateCommentPaginationControls(data.number, data.totalPages);
                } else {
                    tableBody.append('<tr><td colspan="5">신고된 댓글이 없습니다.</td></tr>');
                }
            },
            error: function (xhr, status, error) {
                console.error('Error fetching comment reports:', error);
                $('#commentReportList').html('<tr><td colspan="5">데이터를 불러오는 중 오류가 발생했습니다.</td></tr>');
            }
        });
    }

    function updateCommentPaginationControls(currentPage, totalPages) {
        $('#commentPre').prop('disabled', currentPage === 0);
        $('#commentBack').prop('disabled', currentPage === totalPages - 1);
    }

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
                    fetchCommentReports(commentCurrentPage, commentCurrentSortOrder);
                    loadCommentReportDetail(reportId);
                },
                error: function (xhr, status, error) {
                    console.error('Error fetching comment reports:', error);
                    console.error('Server response:', xhr.responseText);
                    $('#commentReportList').html('<tr><td colspan="5">데이터를 불러오는 중 오류가 발생했습니다. 서버 응답을 확인해 주세요.</td></tr>');
                }
            });
        }
    }

    // 이벤트 리스너들
    $(document).on('click', '#commentReportList tr', function () {
        let reportId = $(this).data('id');
        loadCommentReportDetail(reportId);
    });

    $(document).on('click', '#commentReportDate', function () {
        commentCurrentSortOrder = commentCurrentSortOrder === 'asc' ? 'desc' : 'asc';
        fetchCommentReports(commentCurrentPage, commentCurrentSortOrder);
    });

    $(document).on('click', '#commentPre', function () {
        if (commentCurrentPage > 0) {
            commentCurrentPage--;
            fetchCommentReports(commentCurrentPage, commentCurrentSortOrder);
        }
    });

    $(document).on('click', '#commentBack', function () {
        commentCurrentPage++;
        fetchCommentReports(commentCurrentPage, commentCurrentSortOrder);
    });

    $(document).on('click', '#commentPunish, #commentPass', function () {
        let btnId = $(this).attr('id');
        let reportId = $('#commentReportId').text();
        updateCommentReport(reportId, btnId);
    });

    // 초기 댓글 신고 목록 로드
    $('#comment-tab').on('shown.bs.tab', function (e) {
        fetchCommentReports(commentCurrentPage, commentCurrentSortOrder);
    });
});
