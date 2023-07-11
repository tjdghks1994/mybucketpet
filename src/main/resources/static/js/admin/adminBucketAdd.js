/**
 * 에디터 초기화
 */
function initEditor() {
    let fontList = ['Pretendard'];
    $('#summernote').summernote({
        placeholder: '내용을 작성하세요',
        height: 500,
        fontNames: fontList,
        fontNamesIgnoreCheck: fontList,
        fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72'],
        toolbar: [
            // [groupName, [list of button]]
            ['fontname', ['fontname']],
            ['fontsize', ['fontsize']],
            ['style', ['bold', 'italic', 'underline','strikethrough', 'clear']],
            ['color', ['forecolor','color']],
            ['table', ['table']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['height', ['height']],
            ['insert',['picture','link']]
        ]
    });
}

/**
 * 숨겨진 file 태그 변경 이벤트 감지 함수
 */
function uploadImageFile(file) {
    let uploadImg = file.files[0];
    $('#file-append-name').text(uploadImg['name']);
}

/**
 * 파일 첨부 영역 버튼 클릭 함수
 */
function clickFileBtn() {
    // 실제 숨겨진 file 태그 click
    $('#bucketImageFile').click();
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
    initEditor();
});