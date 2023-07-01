/**
 * 이메일 접두사 input box oninput 이벤트
 */
function emailPrefixEvent() {
    $('#joinEmailPrefix').on('input', function () {
        // css 색상 변경처리
        $('.join-email').css('color', '#343A40');
        // 이메일 형식 검증
        emailValidator();
    });
}
/**
 * 이메일 select box 변경 이벤트
 * */
function emailOptionSelectEvent(selOpt) {
    // css 변경 처리
    $('.join-email').css('color', '#343A40');
    // 이메일 형식 검증
    emailValidator();

    let selOptVal = $(selOpt).val();
    // 직접 입력을 선택한 경우, 입력을 input box 보이도록 설정 및 기존 select box 숨김 처리
    if (selOptVal == '_self') {
        $('.joinMailSelfWrapper').show();
        $('#joinEmailSuffixSelf').focus();
        $('#joinEmailSuffix').hide();
        document.getElementById('joinEmailSuffixSelf').disabled = false;
    }
}
/**
 * 이메일 직접 입력선택 후 x버튼 활성화
 * */
function showClearBtn() {
    if ($('.email-clear-btn').css('display') == 'none') {
        $('.email-clear-btn').show();
    }
    // 이메일 검증
    emailValidator();
}
/**
 * 이메일 직접 입력선택 후 x버튼 클릭 이벤트
 */
function clickClearBtnEvent() {
    $('.email-clear-btn').hide();
    $('.joinMailSelfWrapper').hide();
    $('#joinEmailSuffix').show();
}

/**
 * 이메일 형식 검증기
 */
function emailValidator() {
    let emailPrefixVal = $('#joinEmailPrefix').val();
    let emailSuffixVal = $('#joinEmailSuffix').val();
    let regex = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
    let emailAuthOK = false;

    // 옵셔널 체이닝을 활용 - email 접미사가 null, undefined, 빈 값이 아닌 경우
    if(!!emailSuffixVal?.trim()) {
        // email 접미사가 직접입력을 선택했을 시 올바른 이메일 형식인지 검증
        if (emailSuffixVal == '_self') {
            let selfMailVal = $('#joinEmailSuffixSelf').val();
            let concatSelfMailVal = emailPrefixVal + '@' + selfMailVal;
            // 올바른 이메일 형식인 경우 이메일 인증 버튼 활성화
            if (regex.test(concatSelfMailVal)) {
                emailAuthOK = true;
            }
        } else {
            let emailVal = emailPrefixVal + '@' + emailSuffixVal;
            // 올바른 이메일 형식인 경우 이메일 인증 버튼 활성화
            if (regex.test(emailVal)) {
                emailAuthOK = true;
            }
        }

        if (emailAuthOK) {
            // 이메일 형식이 올바른 경우, 현재 가입된 이메일인지 아닌지 조회
            emailSameCheck();
            return; // 검증 종료
        }
    }

    // 성공 case에 속하지 않는 경우
    // 이메일 형식이 올바르지 않습니다. 문구 출력
    $('#email-fail').show();
    $('#email-exp').hide();
    $('#email-fail').text('이메일 형식이 올바르지 않습니다.');
    document.getElementById('email-auth-btn').disabled = true;
    document.getElementById('email-auth-btn').classList.remove('join-btn-active');
    document.getElementById('email-auth-btn').classList.add('join-btn-disabled');
    document.getElementById('joinEmailPrefix').classList.add('auth-fail');
    document.getElementById('joinEmailSuffixSelf').classList.add('auth-fail');
    document.getElementById('joinEmailSuffix').classList.add('auth-fail');
}
function emailSameCheck() {
    let emailStr = $('#joinEmailPrefix').val();
    const emailSuffixVal = $('#joinEmailSuffix').val();
    if (emailSuffixVal == '_self') {
        let selfMailVal = $('#joinEmailSuffixSelf').val();
        emailStr += '@';
        emailStr += selfMailVal;
    } else {
        emailStr += '@';
        emailStr += emailSuffixVal;
    }

    $.ajax({
        url: contextPath+"/join/mail/"+emailStr,
        type: 'GET',
        accept: 'application/json',
        success: function onData (data) {
            console.log(data);
            // 이미 존재하는 이메일인 경우
            if (!!data['memberId']?.trim()) {
                $('#email-fail').show();
                $('#email-exp').hide();
                if (data['joinPath'] == 'EMAIL') {
                    $('#email-fail').text('이미 가입한 이메일입니다. \'이메일 로그인\'으로 로그인해주세요.');
                } else {
                    $('#email-fail').text('이미 가입한 이메일입니다.');
                }

                document.getElementById('email-auth-btn').disabled = true;
                document.getElementById('email-auth-btn').classList.remove('join-btn-active');
                document.getElementById('email-auth-btn').classList.add('join-btn-disabled');
                document.getElementById('joinEmailPrefix').classList.add('auth-fail');
                document.getElementById('joinEmailSuffixSelf').classList.add('auth-fail');
                document.getElementById('joinEmailSuffix').classList.add('auth-fail');
            } else {
                // 버튼 활성화 처리
                document.getElementById('email-auth-btn').disabled = false;
                document.getElementById('email-auth-btn').classList.remove('join-btn-disabled');
                document.getElementById('email-auth-btn').classList.add('join-btn-active');
                document.getElementById('joinEmailPrefix').classList.remove('auth-fail');
                document.getElementById('joinEmailSuffixSelf').classList.remove('auth-fail');
                document.getElementById('joinEmailSuffix').classList.remove('auth-fail');

                $('#email-fail').hide();
                $('#email-exp').show();
            }
        },
        error: function onError (error) {
            console.error(error);
        }
    });

}

/**
 * 이메일 인증 요청
 */
async function authMailSend() {
    try {
        let emailStr = $('#joinEmailPrefix').val();
        const emailSuffixVal = $('#joinEmailSuffix').val();
        if (emailSuffixVal == '_self') {
            let selfMailVal = $('#joinEmailSuffixSelf').val();
            emailStr += '@';
            emailStr += selfMailVal;
        } else {
            emailStr += '@';
            emailStr += emailSuffixVal;
        }

        const response = await fetch(contextPath+"/join/mail/auth", {
            method: "POST",
            headers: {
                "Content-Type": "text/plain",
            },
            body: emailStr,
        });

        // 인증코드
        const result = await response.text();
        sessionStorage.setItem("authCode", result);
        // 인증코드를 입력받기 위한 input box show
        showInputAuthCode();
    } catch (error) {
        console.error("실패:", error);
    }
}

/**
 * 사용자가 인증 코드를 입력하기 위한 input box show 처리 및 css 조정(클래스 추가, 삭제)
 */
function showInputAuthCode() {
    let prevInputVal = $('#mail-auth-input').val();
    // 이전에 입력된 인증코드가 있다면 clear
    if (!!prevInputVal?.trim()) {
        $('#mail-auth-input').val('');
        $('#auth-non-same').hide();
    }

    $('#mail-auth-wrapper').show();
    // 이메일 인증버튼은 비활성화 처리
    document.getElementById('email-auth-btn').disabled = true;
    document.getElementById('email-auth-btn').classList.remove('join-btn-active');
    document.getElementById('email-auth-btn').classList.add('join-btn-disabled');
    // 인증 코드 입력 input box로 포커스 이동
    $('#mail-auth-input').focus();
}

/**
 * 사용자 인증 코드가 올바른지 확인
 */
function checkAuthCode() {
    let userInputCode = $('#mail-auth-input').val();
    let authCode = sessionStorage.getItem("authCode");

    // 입력된 인증 코드가 메일로 전송된 코드랑 다르다면
    if (userInputCode != authCode) {
        // 인증코드가 잘못되었다는 문구 출력
        $('#auth-non-same').show();
        document.getElementById('mail-auth-input').classList.add('auth-fail');
        // 인증 코드를 체크하기 위한 인증 버튼은 비활성화 처리
        document.getElementById('join-auth-btn').disabled = true;
        document.getElementById('join-auth-btn').classList.remove('active-btn');

        return;
    }

    // 발송한 인증코드와 입력된 인증코드가 일치한 경우
    // 인증 번호 입력 공간 숨김 처리
    $('#mail-auth-wrapper').hide();
    // 인증이 완료되었다고 문구 출력
    $('#email-success').show();
    // 기존 문구 숨김 처리
    $('#email-exp').hide();
    // 이메일 입력 input 비활성화
    document.getElementById('joinEmailPrefix').disabled = true;
    document.getElementById('joinEmailPrefix').classList.add('email-disabled');
    // 이메일 선택 비활성화
    document.getElementById('joinEmailSuffix').disabled = true;
    document.getElementById('joinEmailSuffixSelf').disabled = true;
    document.getElementById('joinEmailSuffix').classList.add('email-disabled');
    // 회원가입 버튼 활성화
    document.getElementById('join-exec-btn').classList.remove('join-btn-disabled');
    document.getElementById('join-exec-btn').classList.add('join-btn-active');
    document.getElementById('join-exec-btn').disabled = false;
    // 비밀번호 입력 input 포커스
    $('#joinPassword').focus();
}

/**
 * 인증번호 입력 시 기존 error 클래스나 비활성 버튼 초기화
 */
function initStyle() {
    // 인증코드가 잘못되었다는 문구 숨김처리
    $('#auth-non-same').hide();
    document.getElementById('mail-auth-input').classList.remove('auth-fail');
    // 인증 코드를 체크하기 위한 인증 버튼은 활성화 처리
    document.getElementById('join-auth-btn').disabled = false;
    document.getElementById('join-auth-btn').classList.add('active-btn');
}

/**
 * 약관동의 - 전체동의 버튼 클릭
 */
function allCheckTerms() {
    $('#allCheck').on('change', function () {
        let allCheckProp = $(this).prop('checked');
        // 전체동의 버튼의 checked 값과 동일하게 변경 처리
        $.each($('.join-check-box .form-check-input'), function () {
            $(this).prop('checked', allCheckProp);
        });
    });
}

/**
 * 전체 동의 버튼 상태 체크
 */
function allCheckStatus() {
    let inputLength = $('.join-check-box .form-check-input').length;
    let checkCount = 0;

    $.each($('.join-check-box .form-check-input'), function () {
        if ($(this).prop('checked')) {
            checkCount++;
        }
    });

    if (inputLength == checkCount) {
        $('#allCheck').prop('checked', true);
    } else {
        $('#allCheck').prop('checked', false);
    }
}

/**
 * th:object와 th:field 기능을 사용하던 중
 * disabled 속성의 값이 true인 태그의 경우 값이 전달되지 않는 문제를 발견하여
 * form 전송 전에 전달되어져야 하는 값을 가진 태그의 disabled 속성 값을 false로 변경 처리
 */
function disabledChange() {
    // th:field를 사용해서 폼 전송 시 disabled 속성 값이 true로 설정되어있으면 값이 전달되지 않는 현상 발견..
    document.getElementById('joinEmailPrefix').disabled = false;
    document.getElementById('joinEmailSuffix').disabled = false;
    document.getElementById('joinEmailSuffixSelf').disabled = false;
}

/**
 * 이메일 인증 처리가 완료되었는지 체크
 * ( 서버에서 검증 후 올바르지 않은 경우 joinMemberForm.html 로 재진입 )
 * 완료된 상태라면 회원가입 버튼 활성화 처리
 */
function checkMailAuthOK() {
    if ($('#mail-auth-wrapper').css('display') == 'none') {
        let authVal = $('#mail-auth-input').val();
        // 메일 인증 Wrapper가 보이지 않는 상태인데 인증 코드 값이 존재한다
        if (!!authVal?.trim()) {
            // 회원가입 버튼 활성화
            document.getElementById('join-exec-btn').classList.remove('join-btn-disabled');
            document.getElementById('join-exec-btn').classList.add('join-btn-active');
            document.getElementById('join-exec-btn').disabled = false;
            // 이메일 인증 관련 태그 비활성화
            document.getElementById('joinEmailPrefix').disabled = true;
            document.getElementById('joinEmailSuffix').disabled = true;
            document.getElementById('joinEmailSuffixSelf').disabled = true;
        }
    }

    // 이메일 직접입력을 한 상태에서 검증오류 발생하여 현재 페이지로 다시 접근했을 때
    // 직접입력한 값이 아니라 select box의 직접입력을 보여주는 문제가 발생하여 수정처리
    let emailSelfVal = $('#joinEmailSuffixSelf').val();
    let selectVal = $('#joinEmailSuffix').val();
    if (!!emailSelfVal?.trim() && selectVal == '_self') {
        $('.joinMailSelfWrapper').show();
        $('#joinEmailSuffix').hide();
    }
}

/**
 * 비밀번호 검증기
 */
function passwordValidator() {
    let passwordVal = $('#joinPassword').val();
    let regex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d~!@#$%^&*()-_+|=]{8,20}$/;
    let errorMsg = passwordErrorMsg;

    // 비밀번호 검증에 성공한 경우
    if (regex.test(passwordVal)) {
        // 서버 검증기를 통한 에러 메시지 표시가 이미 존재한 경우
        if ($('#password-msg').length > 0) {
            $('#password-msg').hide();
            document.getElementById('joinPassword').classList.remove('field-input-error');
        } else {
            $('#js-password-msg').hide();
            document.getElementById('joinPassword').classList.remove('field-input-error');
        }
    } else {
        // 서버 검증기를 통한 에러 메시지 표시가 이미 존재한 경우
        if ($('#password-msg').length > 0) {
            $('#password-msg').hide();
        }
        $('#js-password-msg').show();
        $('#js-password-msg').text(errorMsg);
        document.getElementById('joinPassword').classList.add('field-input-error');
    }
}

/**
 * 비밀번호 확인 검증기
 */
function passwordCheckValidator() {
    let passwordVal = $('#joinPassword').val();
    let passwordCheckVal = $('#joinPasswordCheck').val();
    let errorMsg = passwordNotSameErrorMsg;
    // 비밀번호와 비밀번호 확인의 값이 동일하다면
    if (passwordVal == passwordCheckVal) {
        // 서버 검증기를 통한 에러 메시지 표시가 이미 존재한 경우
        if($('#password-check-pattern-msg').length > 0) {
            $('#password-check-pattern-msg').hide();
        }

        $('#js-password-check-msg').hide();
        document.getElementById('joinPasswordCheck').classList.remove('field-input-error');

    } else {
        // 서버 검증기를 통한 에러 메시지 표시가 이미 존재한 경우
        if ($('#password-check-pattern-msg').length > 0) {
            $('#password-check-pattern-msg').hide();
        }
        $('#js-password-check-msg').show();
        $('#js-password-check-msg').text(errorMsg);
        document.getElementById('joinPasswordCheck').classList.add('field-input-error');
    }
}

/**
 * 닉네임 검증기
 */
function nicknameValidator() {
    let nicknameVal = $('#joinNickname').val();
    let errorMsg = nicknameErrorMsg;

    // 입력된 닉네임이 2~15자가 맞는 경우
    if (nicknameVal.length < 16 && nicknameVal.length > 1) {
        // 서버 검증기를 통한 에러 메시지표시가 이미 존재한 경우
        if ($('#nickname-msg').length > 0) {    // 서버 검증기 에러 메시지 숨기기
            $('#nickname-msg').hide();
        }

        // 닉네임이 이미 존재하는지 체크
        $.ajax({
            url: contextPath+"/join/"+nicknameVal,
            method: 'GET',
            accept: 'text/plain',
            success: function (data) {
                // 이미 존재하는 닉네임이라면 문구 출력
                if (!!data?.trim()) {
                    $('#js-nickname-msg').show();
                    $('#js-nickname-msg').text(nicknameSameErrorMsg);
                    document.getElementById('joinNickname').classList.add('field-input-error');
                } else {
                    $('#js-nickname-msg').hide();
                    document.getElementById('joinNickname').classList.remove('field-input-error');
                }
            },
            error: function (error) {
                console.log(error);
            }
        });

    } else {
        // 서버 검증기를 통한 에러 메시지표시가 이미 존재한 경우
        if ($('#nickname-msg').length > 0) {    // 서버 검증기 에러 메시지 숨기기
            $('#nickname-msg').hide();
        }
        $('#js-nickname-msg').show();
        $('#js-nickname-msg').text(errorMsg);
        document.getElementById('joinNickname').classList.add('field-input-error');
    }
}

window.onload = function () {
    allCheckTerms();
    allCheckStatus();
    emailPrefixEvent();
    checkMailAuthOK();
};
