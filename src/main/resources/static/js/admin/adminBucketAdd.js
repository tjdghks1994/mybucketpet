// 태그 select box 선택한 요소를 담고 있는 배열
let checkTagNameList = new Array();
// 태그 select box 선택한 요소의 값을 담고 있는 배열
let checkTagValueList = new Array();
// 공개 여부 값
let openYnValue = "";
// 추천 여부 값용
let recommendYnValue = "";

/**
 * 버킷 등록 취소
 */
function cancelAdd() {
    let cancelYn = confirm('정말 등록을 취소하고 목록 화면으로 돌아가시겠습니까?');
    if (cancelYn) {
        window.location.replace(contextPath + '/admin/bucket');
    }
}

/**
 * 버킷 등록 전 검증
 * @param thumbnailFile : 썸네일이미지파일
 * @param bucketAdd : 버킷등록에필요한입력정보들이담겨있는객체
 */
function bucketValidator(thumbnailFile, bucketAdd) {
    if (thumbnailFile == undefined || thumbnailFile == null) {
        return false;
    }
    if (bucketAdd['bucketTitle'] == '' || bucketAdd['bucketTitle'] == undefined || bucketAdd['bucketTitle'] == null) {
        return false;
    }
    if (bucketAdd['bucketContents'] == '' || bucketAdd['bucketContents'] == undefined || bucketAdd['bucketContents'] == null) {
        return false;
    }
    if (bucketAdd['openYn'] == '' || bucketAdd['openYn'] == undefined || bucketAdd['openYn'] == null) {
        return false;
    }
    if (bucketAdd['recommendYn'] == '' || bucketAdd['recommendYn'] == undefined || bucketAdd['recommendYn'] == null) {
        return false;
    }
    if (bucketAdd['tagList'].length == 0) {
        return false;
    }

    return true;
}

/**
 * 버킷 등록
 */
function addBucket() {
    let bucketTitle = $('#bucketName').val();
    let bucketContents = $('#summernote').summernote('code');   // 썸머노트 editor 값 가져오기
    let openYn = openYnValue;
    let recommendYn = recommendYnValue;
    let tagList = new Array();
    $.each(checkTagValueList, function (idx, value) {
        let tag = new Object();
        tag.tagId = value;

        tagList.push(tag);
    });
    // 썸네일 이미지 파일
    let thumbnailFile = $('#bucketImageFile')[0].files[0];
    // 버킷 저장할 전송 정보
    let bucketAdd = {
      bucketTitle : bucketTitle,
      bucketContents : bucketContents,
      openYn : openYn,
      recommendYn : recommendYn,
      tagList : tagList
    };

    if (bucketValidator(thumbnailFile, bucketAdd)) {
        // file은 content-type을 multipart/form-data, 그 외의 데이터는 content-type을 application/json 형식으로 따로 보내기 위해 Blob을 사용
        let formData = new FormData();
        formData.append('thumbnailImageFile', thumbnailFile);
        formData.append('bucketAdd', new Blob([JSON.stringify(bucketAdd)], {type: "application/json"}));

        // contentType : false 로 선언 시 content-type 헤더가 multipart/form-data로 전송되게 함일
        // processData : false로 선언 시 formData를 string으로 변환하지 않음
        $.ajax({
            url: contextPath + "/admin/bucket/add",
            method: "post",
            contentType: false,
            processData: false,
            enctype: "multipart/form-data",
            data: formData,
            cache: false,
            success: function (result, statusText, jqXHR) {
                if (result == 'addBucketOK' && jqXHR.status == 201) {
                    alert('버킷 등록에 성공하였습니다.');
                    window.location.href = contextPath + "/admin/bucket";
                } else {
                    alert('버킷 등록에 실패하였습니다. 관리자에게 문의하세요.');
                }
            },
            fail: function (jqXHR, textStatus, errorThrown) {
                alert('버킷 등록에 실패하였습니다. 관리자에게 문의하세요.');
            }
        });
    } else {
        alert('모든 필드를 입력 및 선택해야 합니다.');
    }
}

/**
 * 썸네일 파일 선택 후 x 버튼 클릭 이벤트
 */
function initFileInput() {
    let fileDelYn = confirm("정말 해당 파일 첨부를 취소하시겠습니까?");
    if (fileDelYn) {
        // input file 태그 값 초기화
        $('#bucketImageFile').val('');
        $('#file-append-name').text('');
        $('.file-remove-btn').remove();
    }
}

/**
 * 공개 여부, 추천 여부 버튼 클릭 이벤트
 */
function activeSelectButton(buttonObj) {
    let selectValue = $(buttonObj).val();

    if ($(buttonObj).hasClass('recommendYn')) {
        recommendYnValue = selectValue;
        // 모든 버튼 활성화 클래스 삭제 처리
        $.each($('button.recommendYn'), function (idx, btn) {
            $(btn).removeClass('active');
        });
        // 선택한 버튼만 활성화 클래스 추가
        $(buttonObj).addClass('active');
    } else if ($(buttonObj).hasClass('openYn')) {
        openYnValue = selectValue;
        // 모든 버튼 활성화 클래스 삭제 처리
        $.each($('button.openYn'), function (idx, btn) {
            $(btn).removeClass('active');
        });
        // 선택한 버튼만 활성화 클래스 추가
        $(buttonObj).addClass('active');
    }
}

/**
 * 태그 select box list 클릭 이벤트
 */
function checkTagList(checkObj) {
    // 체크 한 경우
    if (checkObj.checked) {
        // 배열에 저장
        checkTagNameList.push(checkObj.id);
        checkTagValueList.push(checkObj.value);
    } else { // 체크 해제한 경우
        for (let i = 0; i < checkTagNameList.length; i++) {
            if (checkTagNameList[i] === checkObj.id) {
                // 배열에서 제거
                checkTagNameList.splice(i, 1);
                checkTagValueList.splice(i, 1);
                i--;
            }
        }
    }
    // 선택된 태그 명 출력
    $('#bucketTag-box p').text(checkTagNameList.join());
    $('#select-tag-content').text(checkTagNameList.join(", "));
}

/**
 * 태그 select box 이외의 영역 클릭 시
 * select box list 숨김 처리
 */
function hideTagList() {
    // select box 영역 제외하고 클릭했을 시 숨김처리.
    $('body').on('click', function(e){
        if ($(e.target).closest("#bucketTag").length == 0) {
            $('#bucketTag-listUL').hide();
        }
    });

}
/**
 * 태그 select box 클릭 이벤트
 * 목록이 보이는 경우 숨김 처리
 * 목록이 보이지 않는 경우 보이도록 처리
 */
function showTagList() {
    $('#bucketTag-box').on('click', function () {
        if ($('#bucketTag-listUL').css('display') != 'none') {
            $('#bucketTag-listUL').hide();
        } else {
            $('#bucketTag-listUL').show();
        }
    });
}

/**
 * 태그 select box 렌더링
 */
function initTagRendering() {
    $.ajax({
        url : contextPath + "/admin/bucket/tag",
        method : "get",
        contentType: "application/json",
        success: function (data, statusText, jqXHR) {
            $.each(data, function (idx, tagItem) {
                $('#bucketTag ul').append('<li><input type="checkbox" onclick="checkTagList(this);" value=' + tagItem.tagId +
                    ' id='+tagItem.tagName+'> <label for="'+ tagItem.tagName+'">'+ tagItem.tagName+'</label></li>');
            });
        },
        fail: function (jqXHR, textStatus, errorThrown) {
            alert('태그 목록을 가져오는데 오류가 발생했습니다. 관리자에게 문의하세요');
        }
    });
}

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
    let fileExtension = uploadImg.name.substring(uploadImg.name.lastIndexOf('.')+1);
    if (fileExtension == 'jpeg' || fileExtension == 'jpg' || fileExtension == 'png' || fileExtension == 'gif') {
        $('#file-append-name').text(uploadImg['name']);
        if ($('.file-remove-btn').length == 0) {
            $('.file-append-name-wrapper').append('<img class="file-remove-btn" onclick="initFileInput();"' +
                ' src="' + contextPath + '/img/ic_close.svg"/>');
        }
    } else {
        // input file 태그 값 초기화
        $('#bucketImageFile').val('');
        alert('이미지 파일만 업로드 가능합니다 (파일 확장자 : jpeg, jpg, png, gif)');
    }
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
    initTagRendering();
    showTagList();
    hideTagList();
});