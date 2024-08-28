document.addEventListener('DOMContentLoaded', function() {
    const sections = {
        profile: document.querySelector('#profile'),
        reviews: document.querySelector('#reviews'),
        favorite: document.querySelector('#favorite')
    };

    const navItems = document.querySelectorAll('.nav-item');

    function activateTab(tab) {
        // 모든 섹션을 비활성화하고, 클릭한 탭의 섹션만 활성화
        for (let key in sections) {
            sections[key].classList.remove('active');
        }
        sections[tab].classList.add('active');

        // 모든 네비게이션 아이템을 비활성화하고, 클릭한 탭만 활성화
        navItems.forEach(item => {
            item.classList.remove('active');
        });
        document.querySelector(`.nav-item[data-tab="${tab}"]`).classList.add('active');

        // 선택된 탭을 sessionStorage에 저장
        sessionStorage.setItem('activeTab', tab);
    }

    // 로그아웃 버튼 클릭 시 sessionStorage 초기화
    const logoutBtn = document.querySelector('.logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function() {
            sessionStorage.clear();  // sessionStorage를 초기화하여 마지막 탭 정보 제거
        });
    }

    // 기본적으로 "회원정보" 탭 활성화 (로그인 시)
    let activeTab = location.hash.replace('#', '') || sessionStorage.getItem('activeTab') || 'profile';
    activateTab(activeTab);

    // 탭을 클릭할 때 해당 탭을 활성화
    navItems.forEach(item => {
        item.addEventListener('click', function(e) {
            e.preventDefault();
            let tab = this.getAttribute('data-tab');
            activateTab(tab);
            location.hash = tab;  // URL 해시 변경
        });
    });

    // 페이지네이션 링크에 activeTab 파라미터 추가
    const paginationLinks = document.querySelectorAll('.pagination a');
    paginationLinks.forEach(link => {
        const href = new URL(link.href);
        href.searchParams.set('activeTab', activeTab);
        link.href = href.toString();
    });

    // 리뷰 아이템에서 레스토랑 이름 클릭 시 레스토랑 상세 페이지로 이동
    const reviewNames = document.querySelectorAll('.review-item .restaurant-name');
    reviewNames.forEach(name => {
        name.addEventListener('click', function(event) {
            event.stopPropagation();  // 부모 요소의 클릭 이벤트 전파 방지
            const restaurantId = this.closest('.review-item').getAttribute('data-restaurant-id');
            if (restaurantId) {
                window.location.href = '/restaurant/' + restaurantId + '/info';
            }
        });
    });

    // 즐겨찾기된 레스토랑 이름 클릭 시 레스토랑 상세 페이지로 이동
    const favoriteNames = document.querySelectorAll('.favorite-item .restaurant-name');
    favoriteNames.forEach(name => {
        name.addEventListener('click', function(event) {
            event.stopPropagation();  // 부모 요소의 클릭 이벤트 전파 방지
            const restaurantId = this.closest('.favorite-item').getAttribute('data-restaurant-id');
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

    // 즐겨찾기 해제 버튼 로직
    const removeFavoriteButtons = document.querySelectorAll('.remove-favorite-btn');

    removeFavoriteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault(); // 기본 폼 제출 동작을 방지합니다.

            const restaurantId = this.closest('form').querySelector('input[name="restaurantId"]').value;
            const favoriteItem = this.closest('.favorite-item'); // 즐겨찾기 아이템 요소

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
                    'restaurantId': restaurantId
                })
            })
                .then(response => {
                    if (response.ok) {
                        return response.text();
                    } else {
                        throw new Error('Failed to remove favorite');
                    }
                })
                .then(text => {
                    alert('즐겨찾기가 해제되었습니다.');
                    favoriteItem.remove(); // UI에서 해당 아이템 제거

                    // 모든 즐겨찾기 아이템이 제거된 경우 페이지 새로고침
                    if (document.querySelectorAll('.favorite-item').length === 0) {
                        window.location.reload(); // 페이지 새로고침
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('즐겨찾기 해제 중 오류가 발생했습니다.');
                });
        });
    });
});
