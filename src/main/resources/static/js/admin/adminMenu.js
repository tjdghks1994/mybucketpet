/**
 * side 메뉴의 하위 메뉴 실행 함수 (페이지 이동)
 */
function execMenu() {
    $('.admin-side-menu-sub li').on('click', function () {
        let execUrl = $(this).attr('execurl');
        if (execUrl == null || execUrl == undefined || execUrl == '') {
            alert('개발 예정 입니다.');
        } else {
            window.location = contextPath + execUrl;
        }
    });
}

/**
 * side 메뉴의 하위 메뉴 열기/접기 함수
 * parameter : parent   클릭된 side 메뉴
 *           : obj      클릭된 side 메뉴의 하위 ul 태그
 *           : active   클릭된 side 메뉴의 활성화 여부
 *  클릭된 side 메뉴의 활성화 여부가 true - 메뉴 닫기
 *                           false - 메뉴 열기
 */
function subMenuOpenClose(parent, obj, active) {
    if (active) {
        $(parent).removeClass('active');
        $(obj).hide();
    } else {
        $.each($('.admin-side-menu-sub'), function () {
            // 클릭된 side 메뉴의 하위 메뉴 제외 모두 닫기
            if ($(this).css('display') != 'none') {
                $(this).css('display', 'none');
            }
            if ($(this).prev('li').attr('class') == 'active') {
                $(this).prev('li').removeClass('active');
            }
        });

        $(parent).addClass('active');
        $(obj).show();
    }
}

/**
 * side 메뉴 클릭 함수
 * */
function clickSideMenu() {
    $('.admin-side-menu > li').on('click', function () {
        let clickSideMenu = $(this);
        let clickMenuId = $(this).attr('id');
        let active = $(this).attr('class') == 'active';

        if (clickMenuId != 'dashboard-menu') {
            $('.admin-side-menu > ul').each(function (idx, obj) {
                let subMenuForVal = $(obj).attr('for');

                if (clickMenuId == subMenuForVal) {
                    subMenuOpenClose(clickSideMenu, obj, active);
                    return;
                }
            });
        } else {    // 대시보드 메뉴를 클릭한 경우
            console.log('대시보드 메뉴 실행!!');
        }
    });
}

window.onload = function () {
    clickSideMenu();
    execMenu();
};