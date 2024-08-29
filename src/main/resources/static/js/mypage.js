document.addEventListener('DOMContentLoaded', function() {
    const sections = {
        profile: document.querySelector('#profile'),
        reviews: document.querySelector('#reviews'),
        favorite: document.querySelector('#favorite')
    };

    const navItems = document.querySelectorAll('.nav-item');

    function activateTab(tab) {
        for (let key in sections) {
            sections[key].classList.remove('active');
        }
        sections[tab].classList.add('active');

        navItems.forEach(item => {
            item.classList.remove('active');
        });
        document.querySelector(`.nav-item[data-tab="${tab}"]`).classList.add('active');
        sessionStorage.setItem('activeTab', tab);
    }

    const logoutBtn = document.querySelector('.logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function() {
            sessionStorage.clear();  // 로그아웃 시 sessionStorage를 비웁니다.
        });
    }

    // 로그인 시 마지막 탭으로 이동하지만, 로그아웃 후 로그인 시 항상 'profile' 탭이 활성화되도록 설정
    let loggedInBefore = sessionStorage.getItem('loggedInBefore');
    let activeTab = 'profile';  // 기본값은 'profile'

    if (loggedInBefore) {
        activeTab = sessionStorage.getItem('activeTab') || 'profile';
    }

    // 로그인 후에는 loggedInBefore 플래그를 true로 설정
    sessionStorage.setItem('loggedInBefore', true);

    activateTab(activeTab);

    navItems.forEach(item => {
        item.addEventListener('click', function(e) {
            e.preventDefault();
            let tab = this.getAttribute('data-tab');
            activateTab(tab);
            location.hash = tab;
        });
    });

    const paginationLinks = document.querySelectorAll('.pagination a');
    paginationLinks.forEach(link => {
        const href = new URL(link.href);
        href.searchParams.set('activeTab', activeTab);
        link.href = href.toString();
    });

    const reviewNames = document.querySelectorAll('.review-item .restaurant-name');
    reviewNames.forEach(name => {
        name.addEventListener('click', function(event) {
            event.stopPropagation();
            const restaurantId = this.closest('.review-item').getAttribute('data-restaurant-id');
            if (restaurantId) {
                window.location.href = '/restaurant/' + restaurantId + '/info';
            }
        });
    });

    const favoriteNames = document.querySelectorAll('.favorite-item .restaurant-name');
    favoriteNames.forEach(name => {
        name.addEventListener('click', function(event) {
            event.stopPropagation();
            const restaurantId = this.closest('.favorite-item').getAttribute('data-restaurant-id');
            if (restaurantId) {
                window.location.href = '/restaurant/' + restaurantId + '/info';
            }
        });
    });

    const modal = document.getElementById("imageModal");
    const modalImg = document.getElementById("modalImage");
    const captionText = document.getElementById("caption");
    const images = document.querySelectorAll('.review-image');

    images.forEach(img => {
        img.addEventListener('click', function(event) {
            event.stopPropagation();
            modal.style.display = "block";
            modalImg.src = this.src;
            captionText.innerHTML = this.alt;
        });
    });

    const span = document.getElementsByClassName("close")[0];
    span.onclick = function() {
        modal.style.display = "none";
    };

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

    const removeFavoriteForms = document.querySelectorAll('.remove-favorite-form');

    removeFavoriteForms.forEach(form => {
        form.addEventListener('submit', function(e) {
            e.preventDefault();

            if (confirm('즐겨찾기를 해제하시겠습니까?')) {
                const currentPage = parseInt(document.querySelector('input[name="favoritePage"]').value);
                const favoriteItem = this.closest('.favorite-item');
                const remainingItems = document.querySelectorAll('.favorite-item').length;

                fetch(this.action, {
                    method: 'POST',
                    body: new FormData(this)
                })
                    .then(response => {
                        if (response.ok) {
                            return response.text();
                        } else {
                            throw new Error('즐겨찾기 해제 중 오류가 발생했습니다.');
                        }
                    })
                    .then(() => {
                        alert('즐겨찾기가 해제되었습니다.');
                        favoriteItem.remove();

                        if (remainingItems === 1 && currentPage > 0) {
                            window.location.href = `/mypage?favoritePage=${currentPage - 1}&activeTab=favorite`;
                        } else {
                            window.location.href = `/mypage?favoritePage=${currentPage}&activeTab=favorite`;
                        }
                    })
                    .catch(error => {
                        alert(error.message);
                    });
            }
        });
    });
});
