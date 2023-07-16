// 태그 select box 선택한 요소를 담고 있는 배열
let checkTagNameList = new Array();
// 태그 select box 선택한 요소의 값을 담고 있는 배열
let checkTagValueList = new Array();

/**
 * 추천버킷으로 변경
 */
function modifyRecommendBucket() {

}
/**
 * 삭제 버튼 클릭 이벤트
 */
function removeBucketList() {
    let removeYn = confirm('정말 버킷을 삭제하시겠습니까?');
    if (removeYn) {
        // 체크된 버킷 목록을 담고 있는 배열
        let checkBucketList = new Array();
        $.each($('.bucket-check-btn:checked'), function (idx, item) {
            checkBucketList.push($(item).val());
        });

        $.ajax({
            url: contextPath + "/admin/bucket",
            method: "delete",
            contentType: "application/json",
            data: JSON.stringify(checkBucketList),
            success: function (data, statusText, jqXHR) {
                if (data.length > 0) {
                    alert('버킷을 삭제하는데 실패하였습니다. 관리자에게 문의하세요.');
                }
                window.location.replace(contextPath + "/admin/bucket");
            },
            fail: function (jqXHR, textStatus, errorThrown) {
                alert('버킷을 삭제하는데 실패하였습니다. 관리자에게 문의하세요.');
            }
        });
    }
}
/**
 * 모든 버킷 체크 / 언체크 처리
 * @param allCheckBtn : check-all버튼태그
 */
function allCheckBucketList(allCheckBtn) {
    let checkedValue = allCheckBtn.checked;
    $.each($('.bucket-check-btn'), function (idx, item) {
        item.checked = checkedValue;
    });
}
/**
 * 조회 버튼 클릭 후 페이지 전환 시, 이전에 체크한 태그 목록을 체크 처리하는 함수
 */
function initSearchTagList() {
    $.each(searchTagList, function (idx, item) {
        let searchTagId = item.tagId;

        $.each($('.bucket-tag-check'), function (idx, item) {
            if ($(item).val() == searchTagId) {
                $(item).prop('checked', true);
                checkTagNameList.push($(item).attr('id'));
                checkTagValueList.push($(item).val());
            }
        });
    });
    if (checkTagNameList.length > 0 && checkTagValueList.length > 0) {
        // 선택된 태그 명 출력
        $('#condition-tag-box p').text(checkTagNameList.join());
        $('#select-tag-content').text(checkTagNameList.join(", "));
    }
}
/**
 * 조회 버튼 클릭 이벤트
 */
function searchBucketList() {
    let openYnValue = "";   // 공개 여부
    let recommendYnValue = "";  // 추천 여부
    let keywordType = $('#condition-keyword option:selected').val();
    let keywordText = $('#condition-keyword-text').val();   // 키워드 내용
    // 공개 여부
    $.each($('.condition-openYn'), function (idx, item) {
        if ($(item).hasClass('active')) {
            openYnValue = $(item).val();
        }
    });
    // 추천 여부
    $.each($('.condition-recommend'), function (idx, item) {
        if ($(item).hasClass('active')) {
            recommendYnValue = $(item).val();
        }
    });

    let frm = $('#bucket-search-form');
    frm.append('<input type="hidden" name="keywordType" value="' + keywordType + '">');
    frm.append('<input type="hidden" name="keywordText" value="' + keywordText + '">');
    frm.append('<input type="hidden" name="openYn" value="' + openYnValue + '">');
    frm.append('<input type="hidden" name="recommendYn" value="' + recommendYnValue + '">');
    // 태그 선택 목록
    $.each(checkTagValueList, function (idx, value) {
        frm.append('<input type="hidden" name="tagList[' + idx + '].tagId" value="' + value + '">');
    });
    frm.submit();
}
/**
 * 초기화 버튼 클릭 이벤트
 */
function resetCondition() {
    // 공개 여부 모든 버튼 활성화 클래스 삭제 -> 첫번째 버튼인 전체는 활성화 클래스 추가
    $.each($('.condition-openYn'), function (idx, item) {
        if (idx != 0) {
            $(item).removeClass('active');
        } else {
            $(item).addClass('active');
        }
    });
    // 추천 여부 모든 버튼 활성화 클래스 삭제 -> 첫번째 버튼인 전체는 활성화 클래스 추가
    $.each($('.condition-recommend'), function (idx, item) {
        if (idx != 0) {
            $(item).removeClass('active');
        } else {
            $(item).addClass('active');
        }
    });
    // Tag 배열 값 초기화
    checkTagNameList = new Array();
    checkTagValueList = new Array();
    // 문구 초기화
    $('#condition-tag-box p').text('태그 선택');
    $('#select-tag-content').text('');
    // 검색어 초기화
    $('#condition-keyword-text').val('');
}
/**
 * 추천 여부 버튼 클릭 이벤트
 * @param btn : 클릭된추천여부버튼 (전체, 추천, 비추천)
 */
function clickRecommendYn(btn) {
    $.each($('.condition-recommend'), function (idx, item) {
        $(item).removeClass('active');
    });
    // 클릭된 버튼 활성화 클래스 추가
    $(btn).addClass('active');
}
/**
 * 공개 여부 버튼 클릭 이벤트
 * @param btn : 클릭된공개여부버튼 (전체, 공개, 비공개)
 */
function clickOpenYn(btn) {
    $.each($('.condition-openYn'), function (idx, item) {
        $(item).removeClass('active');
    });
    // 클릭된 버튼 활성화 클래스 추가
    $(btn).addClass('active');
}
/**
 * 페이지 버튼 클릭 시 페이지 이동
 * @param pageBtn : 클릭된페이징버튼
 */
function pageMove(pageBtn) {
    let pageVal = $(pageBtn).attr('value');

    let openYnValue = "";   // 공개 여부
    let recommendYnValue = "";  // 추천 여부
    let keywordType = $('#condition-keyword option:selected').val();
    let keywordText = $('#condition-keyword-text').val();   // 키워드 내용
    // 공개 여부
    $.each($('.condition-openYn'), function (idx, item) {
        if ($(item).hasClass('active')) {
            openYnValue = $(item).val();
        }
    });
    // 추천 여부
    $.each($('.condition-recommend'), function (idx, item) {
        if ($(item).hasClass('active')) {
            recommendYnValue = $(item).val();
        }
    });

    let frm = $('#bucket-search-form');
    frm.append('<input type="hidden" name="keywordType" value="' + keywordType + '">');
    frm.append('<input type="hidden" name="keywordText" value="' + keywordText + '">');
    frm.append('<input type="hidden" name="openYn" value="' + openYnValue + '">');
    frm.append('<input type="hidden" name="recommendYn" value="' + recommendYnValue + '">');
    // 태그 선택 목록
    $.each(checkTagValueList, function (idx, value) {
        frm.append('<input type="hidden" name="tagList[' + idx + '].tagId" value="' + value + '">');
    });
    // 페이지 번호
    frm.append('<input type="hidden" name="pageNum" value="' + pageVal + '">');
    frm.submit();
}
/**
 * 태그 select box list 클릭 이벤트
 * @param checkObj : 클릭된태그목록input태그
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
    $('#condition-tag-box p').text(checkTagNameList.join());
    $('#select-tag-content').text(checkTagNameList.join(", "));
}
/**
 * 태그 select box 이외의 영역 클릭 시
 * select box list 숨김 처리
 */
function hideTagList() {
    // select box 영역 제외하고 클릭했을 시 숨김처리.
    $('body').on('click', function(e){
        if ($(e.target).closest("#condition-tag").length == 0) {
            $('#condition-tag-list').hide();
        }
    });
}
/**
 * 태그 select box 클릭 이벤트
 * 목록이 보이는 경우 숨김 처리
 * 목록이 보이지 않는 경우 보이도록 처리
 */
function showTagList() {
    $('#condition-tag-box').on('click', function () {
        if ($('#condition-tag-list').css('display') != 'none') {
            $('#condition-tag-list').hide();
        } else {
            $('#condition-tag-list').show();
        }
    });
}
/**
 * 페이지 진입 시 태그 select box 렌더링
 */
function initTagRendering() {
    $.ajax({
        url : contextPath + "/admin/bucket/tag",
        method : "get",
        contentType: "application/json",
        success: function (data, statusText, jqXHR) {
            $.each(data, function (idx, tagItem) {
                $('#condition-tag-list').append('<li><input type="checkbox" onclick="checkTagList(this);" value=' + tagItem.tagId +
                    ' id='+tagItem.tagName+' class="bucket-tag-check"> <label for="'+ tagItem.tagName+'">'+ tagItem.tagName+'</label></li>');
            });
            // 페이지 진입 시 이전 태그 선택 조건이 있는 경우 선택
            initSearchTagList();
        },
        fail: function (jqXHR, textStatus, errorThrown) {
            alert('태그 목록을 가져오는데 오류가 발생했습니다. 관리자에게 문의하세요');
        }
    });
}
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
    initTagRendering();
    showTagList();
    hideTagList();
});