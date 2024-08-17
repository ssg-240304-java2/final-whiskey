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
function loadRestaurantInfo() {
    // TODO: 백엔드 API를 통해 음식점 정보를 가져와 표시하는 로직 구현
    // 예시 코드:
    fetch("/api/restaurant/info")
        .then((response) => response.json())
        .then((data) => {
            document.getElementById("displayRestaurantName").textContent = data.name;
            document.getElementById("displayRestaurantNumber").textContent = data.number;
            document.getElementById("displayRestaurantAddress").textContent = data.address;
            document.getElementById("displayBusinessNumber").textContent = data.businessNumber;
            document.getElementById("displayOwnerName").textContent = data.ownerName;
            document.getElementById("displayCategory").textContent = data.category;

            // 음식점 이미지 설정
            document.getElementById("restaurantImage").src = data.imageUrl || "https://via.placeholder.com/300x200";

            const operatingHoursList = document.getElementById("displayOperatingHours");
            operatingHoursList.innerHTML = "";
            for (const [day, hours] of Object.entries(data.operatingHours)) {
                if (hours) {
                    const li = document.createElement("li");
                    li.textContent = `${day}: ${hours.open} - ${hours.close}`;
                    operatingHoursList.appendChild(li);
                }
            }

            // 모달 폼에도 데이터 설정
            document.getElementById("restaurantName").value = data.name;
            document.getElementById("restaurantNumber").value = data.number;
            document.getElementById("restaurantAddress").value = data.address;
            document.getElementById("businessNumber").value = data.businessNumber;
            document.getElementById("ownerName").value = data.ownerName;
            document.getElementById("category").value = data.category;

            // 영업 시간 정보 설정
            const days = ["월", "화", "수", "목", "금", "토", "일"];
            days.forEach((day) => {
                const operateCheckbox = document.getElementById(`operate${day}`);
                const openTimeSelect = document.getElementById(`openTime${day}`);
                const closeTimeSelect = document.getElementById(`closeTime${day}`);

                if (data.operatingHours[day]) {
                    operateCheckbox.checked = true;
                    openTimeSelect.value = data.operatingHours[day].open;
                    closeTimeSelect.value = data.operatingHours[day].close;
                } else {
                    operateCheckbox.checked = false;
                    openTimeSelect.value = "";
                    closeTimeSelect.value = "";
                }
            });
        })
        .catch((error) => console.error("음식점 정보 로드 실패:", error));
}

function sendModificationRequest() {
    const formData = new FormData(document.getElementById("restaurantInfoForm"));
    formData.append("modificationReason", document.getElementById("modificationReason").value);

    // 영업 시간 정보 추가
    const operatingHours = {};
    const days = ["월", "화", "수", "목", "금", "토", "일"];
    days.forEach((day) => {
        const operateCheckbox = document.getElementById(`operate${day}`);
        if (operateCheckbox.checked) {
            operatingHours[day] = {
                open: document.getElementById(`openTime${day}`).value,
                close: document.getElementById(`closeTime${day}`).value,
            };
        }
    });
    formData.append("operatingHours", JSON.stringify(operatingHours));

    // TODO: 백엔드 API를 통해 음식점 정보 수정 요청을 보내는 로직 구현
    // 예시 코드:
    // fetch('/api/restaurant/request-modification', {
    //     method: 'POST',
    //     body: formData
    // })
    //     .then(response => response.json())
    //     .then(data => {
    //         alert('음식점 정보 수정 요청이 성공적으로 전송되었습니다. 관리자 검토 후 처리됩니다.');
    //         $('#modificationRequestModal').modal('hide');
    //     })
    //     .catch(error => console.error('음식점 정보 수정 요청 실패:', error));

    $("#modificationRequestModal").modal("hide");
}

function sendDeletionRequest() {
    const reason = document.getElementById("deletionReason").value;
    // TODO: 백엔드 API를 통해 음식점 삭제 요청을 보내는 로직 구현
    // 예시 코드:
    // fetch('/api/restaurant/request-deletion', {
    //     method: 'POST',
    //     headers: {
    //         'Content-Type': 'application/json',
    //     },
    //     body: JSON.stringify({ reason: reason })
    // })
    //     .then(response => response.json())
    //     .then(data => {
    //         alert('음식점 삭제 요청이 성공적으로 전송되었습니다. 관리자 검토 후 처리됩니다.');
    //         $('#deletionRequestModal').modal('hide');
    //     })
    //     .catch(error => console.error('음식점 삭제 요청 실패:', error));

    $("#deletionRequestModal").modal("hide");
}
