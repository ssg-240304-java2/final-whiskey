document.addEventListener('DOMContentLoaded', function() {
    // 리뷰 아이템을 클릭했을 때 레스토랑 상세 페이지로 이동
    const reviewItems = document.querySelectorAll('.review-item');
    reviewItems.forEach(item => {
        item.addEventListener('click', function() {
            const restaurantId = this.getAttribute('data-restaurant-id');
            if (restaurantId) {
                window.location.href = '/restaurant/' + restaurantId + '/info';
            }
        });
    });

    // 북마크 아이템을 클릭했을 때 레스토랑 상세 페이지로 이동
    const bookmarkItems = document.querySelectorAll('.bookmark-item');
    bookmarkItems.forEach(item => {
        item.addEventListener('click', function() {
            const restaurantId = this.getAttribute('data-restaurant-id');
            if (restaurantId) {
                window.location.href = '/restaurant/' + restaurantId + '/info';
            }
        });
    });

    // 이미지 모달
    const modal = document.getElementById("imageModal");
    const modalImg = document.getElementById("modalImage");
    const captionText = document.getElementById("caption");
    const images = document.querySelectorAll('.review-image');

    images.forEach(img => {
        img.addEventListener('click', function(event) {
            event.stopPropagation(); // 부모 요소의 클릭 이벤트 전파 방지
            modal.style.display = "block";
            modalImg.src = this.src;
            captionText.innerHTML = this.alt;
        });
    });

    const span = document.getElementsByClassName("close")[0];
    span.onclick = function() {
        modal.style.display = "none";
    };

    // 프로필 네비게이션 탭 전환
    const navItems = document.querySelectorAll('.profile-nav li');
    const contentSections = document.querySelectorAll('.content-section');

    navItems.forEach(item => {
        item.addEventListener('click', function(e) {
            e.preventDefault();
            const tab = this.getAttribute('data-tab');

            navItems.forEach(navItem => navItem.classList.remove('active'));
            this.classList.add('active');

            contentSections.forEach(section => {
                section.classList.remove('active');
                if (section.id === tab) {
                    section.classList.add('active');
                }
            });
        });
    });

    // 프로필 수정 모달 관리
    const modalBasic = document.getElementById('editProfileModalBasic');
    const modalSocial = document.getElementById('editProfileModalSocial');
    const btn = document.querySelector('.edit-profile-btn');
    const spanBasic = modalBasic ? modalBasic.getElementsByClassName('close')[0] : null;
    const spanSocial = modalSocial ? modalSocial.getElementsByClassName('close')[0] : null;
    const formBasic = document.getElementById('editProfileFormBasic');
    const formSocial = document.getElementById('editProfileFormSocial');

    if (btn) {
        btn.onclick = function() {
            if (modalBasic) {
                modalBasic.style.display = "block";
                setTimeout(() => modalBasic.classList.add('show'), 10);
            } else if (modalSocial) {
                modalSocial.style.display = "block";
                setTimeout(() => modalSocial.classList.add('show'), 10);
            }
        };
    }

    if (spanBasic) {
        spanBasic.onclick = function() {
            modalBasic.classList.remove('show');
            setTimeout(() => modalBasic.style.display = "none", 300);
        };
    }

    if (spanSocial) {
        spanSocial.onclick = function() {
            modalSocial.classList.remove('show');
            setTimeout(() => modalSocial.style.display = "none", 300);
        };
    }

    if (formBasic) {
        formBasic.onsubmit = function(e) {
            modalBasic.classList.remove('show');
            setTimeout(() => modalBasic.style.display = "none", 300);
        };
    }

    if (formSocial) {
        formSocial.onsubmit = function(e) {
            modalSocial.classList.remove('show');
            setTimeout(() => modalSocial.style.display = "none", 300);
        };
    }

    window.onclick = function(event) {
        if (modalBasic && event.target == modalBasic) {
            modalBasic.classList.remove('show');
            setTimeout(() => modalBasic.style.display = "none", 300);
        } else if (modalSocial && event.target == modalSocial) {
            modalSocial.classList.remove('show');
            setTimeout(() => modalSocial.style.display = "none", 300);
        }
    };

    // 로그인 ID 중복 확인 로직
    document.getElementById('loginIdCheckBtn').addEventListener('click', function() {
        const loginId = document.getElementById('basicLoginId').value;

        if (!loginId) {
            document.getElementById('loginId-check-message').textContent = '로그인 ID를 입력하세요.';
            document.getElementById('loginId-check-message').style.color = 'red';
            document.getElementById('loginId-check-message').style.display = 'block';
            return;
        }

        const xhr = new XMLHttpRequest();
        xhr.open('POST', '/checkLoginId', true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    const response = JSON.parse(xhr.responseText);
                    if (response.exists) {
                        document.getElementById('loginId-check-message').textContent = '이미 사용 중인 로그인 ID입니다.';
                        document.getElementById('loginId-check-message').style.color = 'red';
                        document.getElementById('loginId-check-message').style.display = 'block';
                        document.getElementById('submit-btn').disabled = true;
                    } else {
                        document.getElementById('loginId-check-message').textContent = '사용할 수 있는 로그인 ID입니다.';
                        document.getElementById('loginId-check-message').style.color = 'green';
                        document.getElementById('loginId-check-message').style.display = 'block';
                        document.getElementById('submit-btn').disabled = false;
                    }
                } else {
                    console.error('Server error or other issue.', xhr.status, xhr.statusText);
                }
            }
        };
        xhr.send('loginId=' + encodeURIComponent(loginId));
    });

    // 비밀번호 확인 로직
    document.getElementById('basicPassword').addEventListener('keyup', checkPasswords);
    document.getElementById('basicConfirmPassword').addEventListener('keyup', checkPasswords);

    function checkPasswords() {
        const password = document.getElementById('basicPassword').value;
        const confirmPassword = document.getElementById('basicConfirmPassword').value;

        if (password.length > 0 && password !== confirmPassword) {
            document.getElementById('password-match-message').textContent = '비밀번호가 일치하지 않습니다.';
            document.getElementById('password-match-message').style.display = 'block';
            document.getElementById('submit-btn').disabled = true;
        } else {
            document.getElementById('password-match-message').style.display = 'none';
            document.getElementById('submit-btn').disabled = false;
        }
    }

    // 북마크 해제 버튼 로직
    const removeFavoriteButtons = document.querySelectorAll('.remove-favorite-btn');

    removeFavoriteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault(); // 기본 폼 제출 동작을 방지합니다.

            const restaurantId = this.closest('form').querySelector('input[name="restaurantId"]').value;
            const bookmarkPage = this.closest('form').querySelector('input[name="bookmarkPage"]').value;
            const bookmarkSize = this.closest('form').querySelector('input[name="bookmarkSize"]').value;

            if (!restaurantId) {
                alert('레스토랑 ID가 유효하지 않습니다.');
                return;
            }

            fetch('/mypage/remove-favorite', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: new URLSearchParams({
                    'restaurantId': restaurantId,
                    'bookmarkPage': bookmarkPage,
                    'bookmarkSize': bookmarkSize
                })
            })
                .then(response => {
                    return response.text().then(text => {
                        if (response.ok) {
                            alert('북마크가 해제되었습니다.');
                            window.location.reload(); // 페이지 새로고침
                        } else {
                            alert(`북마크 해제에 실패했습니다: ${text}`);
                        }
                    });
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('북마크 해제 중 오류가 발생했습니다.');
                });
        });
    });
});
