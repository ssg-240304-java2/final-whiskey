document.addEventListener('DOMContentLoaded', function() {
    var modal = document.getElementById("categoryModal");
    var btn = document.getElementById("showAllCategories");
    var span = document.getElementsByClassName("close")[0];

    btn.onclick = function() {
        modal.style.display = "block";
    }

    span.onclick = function() {
        modal.style.display = "none";
    }

    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }

    // 결과 카드에 호버 효과 추가
    const resultCards = document.querySelectorAll('.result-card');
    resultCards.forEach(card => {
        card.addEventListener('mouseenter', () => {
            card.style.boxShadow = '0 6px 12px rgba(0,0,0,0.15)';
        });
        card.addEventListener('mouseleave', () => {
            card.style.boxShadow = '';
        });
    });
});
