
$(document).ready(function () {
    let reviewCurrentSortOrder = 'asc';
    let reviewCurrentPage = 0;
    const reviewPageSize = 10;

    function fetchReviewReports(page, sortOrder = 'asc') {
        $.ajax({
            url: /*[[@{/reviewreport/list}]]*/ '/reviewreport/list',
            data: { page: page, sortOrder: sortOrder },
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                console.log('Review reports data:', data); // 로그 추가
                let tableBody = $('#reviewReportList');
                tableBody.empty();
                if (data && data.content && data.content.length > 0) {
                    data.content.forEach(function (report) {
                        let formattedDate = new Date(report.reportedAt).toLocaleDateString();
                        const row = `
            <tr data-id="${report.id}">
                <td>${report.id}</td>
                <td>${report.title}</td>
                <td>${report.content}</td>
                <td>${report.review.content}</td>
                <td>${formattedDate}</td>
                <td>${report.checked ? '처리 완료' : '처리 전'}</td>
            </tr>`;
                        tableBody.append(row);
                    });
                    updateReviewPaginationControls(data.number, data.totalPages);
                } else {
                    console.log('No review reports found'); // 로그 추가
                    tableBody.append('<tr><td colspan="5">신고된 리뷰가 없습니다.</td></tr>');
                }
            },
            error: function (xhr, status, error) {
                console.error('Error fetching review reports:', error);
                console.error('XHR status:', status);
                console.error('XHR response:', xhr.responseText);
                $('#reviewReportList').html('<tr><td colspan="5">데이터를 불러오는 중 오류가 발생했습니다. 자세한 내용은 콘솔을 확인해주세요.</td></tr>');
            }
        });
    }

    function updateReviewPaginationControls(currentPage, totalPages) {
        $('#reviewPre').prop('disabled', currentPage === 0);
        $('#reviewBack').prop('disabled', currentPage === totalPages - 1);
    }

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
                    fetchReviewReports(reviewCurrentPage, reviewCurrentSortOrder);
                    loadReviewReportDetail(reportId);
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

    $(document).on('click', '#reviewReportDate', function () {
        reviewCurrentSortOrder = reviewCurrentSortOrder === 'asc' ? 'desc' : 'asc';
        fetchReviewReports(reviewCurrentPage, reviewCurrentSortOrder);
    });

    $(document).on('click', '#reviewPre', function () {
        if (reviewCurrentPage > 0) {
            reviewCurrentPage--;
            fetchReviewReports(reviewCurrentPage, reviewCurrentSortOrder);
        }
    });

    $(document).on('click', '#reviewBack', function () {
        reviewCurrentPage++;
        fetchReviewReports(reviewCurrentPage, reviewCurrentSortOrder);
    });

    $(document).on('click', '#reviewPunish, #reviewPass', function () {
        let btnId = $(this).attr('id');
        let reportId = $('#reviewReportId').text();
        updateReviewReport(reportId, btnId);
    });

    // 초기 리뷰 신고 목록 로드
    $('#review-tab').on('shown.bs.tab', function (e) {
        fetchReviewReports(reviewCurrentPage, reviewCurrentSortOrder);
    });
});
