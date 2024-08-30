// 음식점 정보 관리 기능
function initRestaurantInfo() {
    console.log('Restaurant Info initialized');
    // 음식점 정보 로드
    loadRestaurantInfo();

    // 수정 요청 버튼 클릭 이벤트
    const requestModificationButton = document.getElementById("requestModification");
    if (requestModificationButton) {
        requestModificationButton.addEventListener("click", () => {
            $("#modificationRequestModal").modal("show");
        });
    }

    // 수정 요청 보내기 버튼 클릭 이벤트
    const sendModificationRequestButton = document.getElementById("sendModificationRequest");
    if (sendModificationRequestButton) {
        sendModificationRequestButton.addEventListener("click", sendModificationRequest);
    }

    // 삭제 요청 버튼 클릭 이벤트
    const requestDeletionButton = document.getElementById("requestDeletion");
    if (requestDeletionButton) {
        requestDeletionButton.addEventListener("click", () => {
            $("#deletionRequestModal").modal("show");
        });
    }

    // 삭제 요청 확인 버튼 클릭 이벤트
    const confirmDeletionRequestButton = document.getElementById("confirmDeletionRequest");
    if (confirmDeletionRequestButton) {
        confirmDeletionRequestButton.addEventListener("click", sendDeletionRequest);
    }
}

// 페이지 로드 시 음식점 정보 초기화
document.addEventListener("DOMContentLoaded", initRestaurantInfo);

// 음식점 정보를 로드하고 화면에 표시하는 함수
function loadRestaurantInfo() {
    // TODO: 백엔드 개발 - GET /api/restaurant/info 엔드포인트 구현
    // 응답 데이터 형식: { name, number, address, ownerName, category, imageUrl, operatingHours }
    // operatingHours 형식: { "월": { open: "09:00", close: "18:00" }, ... }

    // 카테고리 목록 불러오기
    if (document.getElementById("category")) {
        fetch("/api/restaurant/category")
            .then((response) => response.json())
            .then((data) => {
                var categoryList = document.getElementById("category");
                categoryList.innerHTML = `<option disabled hidden>카테고리 선택</option>`;
                data.forEach((category) => {
                    var option = document.createElement("option");
                    option.value = category.value;
                    option.textContent = category.name;
                    categoryList.appendChild(option);
                });
            });
    }

    fetch("/api/restaurant/info")
        .then((response) => response.json())
        .then((data) => {
            // 음식점 기본 정보 표시

            document.getElementById("displayRestaurantName").textContent = data.name;
            if (document.getElementById("displayRestaurantNumber") !== null) {
                document.getElementById("displayRestaurantNumber").textContent = data.number;
            }
            if (document.getElementById("displayRestaurantAddress") !== null) {
                document.getElementById("displayRestaurantAddress").textContent = data.address;
            }
            if (document.getElementById("displayOwnerName") !== null) {
                document.getElementById("displayOwnerName").textContent = data.ownerName;
            }
            if (document.getElementById("displayCategory") !== null) {
                document.getElementById("displayCategory").textContent = data.category;
            }
            // 음식점 이미지 설정
            if (document.getElementById("restaurantImage") !== null) {
                document.getElementById("restaurantImage").src = data.imageUrl || "https://via.placeholder.com/300x200";
            }

            // 영업 시간 정보 표시
            if (document.getElementById("displayOperatingHours") !== null) {
                const days = ["월", "화", "수", "목", "금", "토", "일"];
                const operatingHoursList = document.getElementById("displayOperatingHours");
                operatingHoursList.innerHTML = "";
                for (let day of days) {
                    var li = document.createElement("li");
                    li.textContent = `${day} : ` + data.operatingHours[day];
                    operatingHoursList.appendChild(li);
                }

                if ($('.nav-item')[4].children[0].classList.contains('active')) {
                    // 영업 시간 정보 설정
                    days.forEach((day) => {
                        const operateCheckbox = document.getElementById(`operate${day}`);
                        const openTimeSelect = document.getElementById(`openTime${day}`);
                        const closeTimeSelect = document.getElementById(`closeTime${day}`);

                        console.log(data.operatingHours[day] === "휴무");

                        if (data.operatingHours[day] !== "휴무") {
                            var hour = data.operatingHours[day].split(" - ");
                            console.log(hour);
                            operateCheckbox.checked = true;
                            for (let option of openTimeSelect.options) {
                                if (option.textContent === hour[0]) {
                                    option.selected = true;
                                }
                            }
                            for (let option of closeTimeSelect.options) {
                                if (option.textContent === hour[1]) {
                                    option.selected = true;
                                }
                            }
                        } else {
                            operateCheckbox.checked = false;
                        }
                    });
                }
            }

            // 수정 모달 폼에 데이터 설정
            if (document.getElementById("restaurantName") !== null) {
                document.getElementById("restaurantName").value = data.name;
            }
            if (document.getElementById("restaurantNumber") !== null) {
                document.getElementById("restaurantNumber").value = data.number;
            }
            if (document.getElementById("restaurantAddress") !== null) {
                document.getElementById("restaurantAddress").value = data.address;
            }
            if (document.getElementById("ownerName") !== null) {
                document.getElementById("ownerName").value = data.ownerName;
            }
            if (document.getElementById("category") !== null) {
                document.getElementById('category').options.forEach((option) => {
                    if (option.value === data.category) {
                        option.selected = true;
                    }
                });
            }
        })
        .catch((error) => console.error("음식점 정보 로드 실패:", error));
}

// 음식점 정보 수정 함수
function sendModificationRequest() {
    const formData = new FormData(document.getElementById("restaurantInfoForm"));
    formData.append("modificationReason", document.getElementById("modificationReason").value);

    // 영업 시간 정보 추가
    const operatingHours = {};
    const days = ["월", "화", "수", "목", "금", "토", "일"];
    days.forEach((day) => {
        const operateCheckbox = document.getElementById(`operate${day}`);
        if (operateCheckbox.checked) {
            var dayOpenOption = document.getElementById(`openTime${day}`);
            var dayCloseOption = document.getElementById(`closeTime${day}`);
            operatingHours[day] = {
                isOpen: true,
                open: dayOpenOption.options[dayOpenOption.selectedIndex].textContent,
                close: dayCloseOption.options[dayCloseOption.selectedIndex].textContent,
            };
        } else {
            operatingHours[day] = {
                isOpen: false,
                open: "",
                close: "",
            };
        }
    });

    var restaurantId = document.getElementById("restaurantId").value;

    // TODO: 백엔드 개발 - POST /api/restaurant/modification 엔드포인트 구현
    $.ajax({
        url: `/api/restaurant/modification/${restaurantId}`,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({operatingHours}),
        success: function (response) {
            console.log('Operating hours updated successfully:', response);
            $('#modificationRequestModal').modal('hide');
            loadRestaurantInfo();
        },
        error: function (xhr, status, error) {
            console.error('Error updating operating hours:', error);
            $('#modificationRequestModal').modal('hide');
        }
    });

}

// 음식점 삭제 요청을 보내는 함수
function sendDeletionRequest() {
    const reason = document.getElementById("deletionReason").value;

    // TODO: 백엔드 개발 - POST /api/restaurant/request-deletion 엔드포인트 구현
    // 요청 데이터: { reason: "삭제 사유" }
    // 응답 데이터: { success: true/false, message: "처리 결과 메시지" }
    fetch('/api/restaurant/request-deletion', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({reason: reason})
    })
        .then(response => response.json())
        .then(data => {
            alert('음식점 삭제 요청이 성공적으로 전송되었습니다. 관리자 검토 후 처리됩니다.');
            $('#deletionRequestModal').modal('hide');
        })
        .catch(error => console.error('음식점 삭제 요청 실패:', error));
}
