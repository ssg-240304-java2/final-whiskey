document.addEventListener('DOMContentLoaded', function () {
    RestaurantDetail.initNotices();
});

RestaurantDetail.initNotices = function () {
    const restaurantId = document.getElementById('restaurantId').value;

    loadNotices(restaurantId);
};

// 공지사항 목록 조회
function loadNotices(restaurantId) {
    fetch(`/restaurant/${restaurantId}/user-notice`)
    .then(response =>
        response.json()
    )
    .then(notices => {
        const noticeContainer = document.querySelector('.notices-scroll');
        noticeContainer.innerHTML = '';

        notices.forEach(notice => {
            const noticeCard = document.createElement('div');
            noticeCard.classList.add('notice-card');
            noticeCard.innerHTML = `
                <h3>${notice.title}</h3>
                <p>${notice.content}</p>
                <span class="notice-date">${new Date(notice.createdAt).toLocaleDateString()}</span>
            `;
            noticeContainer.appendChild(noticeCard);
        });
    })
    .catch(error =>
        console.error('공지사항 오류', error)
    );
}