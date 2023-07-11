/**
 * 버킷 등록 페이지 전환
 */
function goBucketAddForm() {
    window.location.href = contextPath + "/admin/bucket/add";
}

/**
 * 첫 페이지 진입 시 자신의 메뉴 open 함수
 */
function openMenu() {
    $.each($('.admin-side-menu li'), function () {
        if ($(this).attr('id') == 'bucket-manage-menu') {
            $(this).addClass('active');
            $(this).next('ul').show();

            $.each($(this).next('ul').children('li'), function () {
                let menuId = $(this).attr('id');
                // 메뉴 ID가 버킷 관리의 메뉴 ID 값
                if (menuId == 'bucket-manage-sub-menu') {
                    $(this).addClass('active');
                }
            });
        }
    });
}

/**
 * 상단 헤더 메뉴 타이틀 변경
 */
function changeMenuTitle() {
    $('.admin-header-wrapper span').text('버킷 관리');
}

$(function () {
    openMenu();
    changeMenuTitle();
});