/**
 * 이메일 접두사 input box focus 이벤트
 */
function emailPrefixEvent() {
    // css 색상 변경처리
    $('.join-email').css('color', '#343A40');
    // 이메일 형식 검증
    emailValidator();
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
        $('#joinMailSelf').focus();
        $('#joinMailSelect').hide();
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
    $('#joinMailSelect').show();
}

/**
 * 이메일 형식 검증기
 */
function emailValidator() {
    let emailPrefixVal = $('#joinMail').val();
    let emailSuffixVal = $('#joinMailSelect').val();
    let regex = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
    let emailAuthOK = false;

    // 옵셔널 체이닝을 활용 - email 접미사가 null, undefined, 빈 값이 아닌 경우
    if(!!emailSuffixVal?.trim()) {
        // email 접미사가 직접입력을 선택했을 시 올바른 이메일 형식인지 검증
        if (emailSuffixVal == '_self') {
            let selfMailVal = $('#joinMailSelf').val();
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
            // 버튼 활성화 처리
            document.getElementById('email-auth-btn').disabled = false;
            document.getElementById('email-auth-btn').classList.remove('join-btn-disabled');
            document.getElementById('email-auth-btn').classList.add('join-btn-active');
            document.getElementById('joinMail').classList.remove('auth-fail');
            document.getElementById('joinMailSelf').classList.remove('auth-fail');
            document.getElementById('joinMailSelect').classList.remove('auth-fail');

            $('#email-fail').hide();
            $('#email-exp').show();

            return; // 검증 종료
        }
    }

    // 성공 case에 속하지 않는 경우
    // 이메일 형식이 올바르지 않습니다. 문구 출력
    $('#email-fail').show();
    $('#email-exp').hide();
    document.getElementById('email-auth-btn').disabled = true;
    document.getElementById('email-auth-btn').classList.remove('join-btn-active');
    document.getElementById('email-auth-btn').classList.add('join-btn-disabled');
    document.getElementById('joinMail').classList.add('auth-fail');
    document.getElementById('joinMailSelf').classList.add('auth-fail');
    document.getElementById('joinMailSelect').classList.add('auth-fail');
}

/**
 * 이메일 인증 요청
 */
async function authMailSend() {
    try {
        let emailStr = $('#joinMail').val();
        const emailSuffixVal = $('#joinMailSelect').val();
        if (emailSuffixVal == '_self') {
            let selfMailVal = $('#joinMailSelf').val();
            emailStr += '@';
            emailStr += selfMailVal;
        } else {
            emailStr += '@';
            emailStr += emailSuffixVal;
        }

        const response = await fetch(contextPath+"/login/join/mailAuth", {
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
    document.getElementById('joinMail').disabled = true;
    document.getElementById('joinMail').classList.add('email-disabled');
    // 이메일 선택 비활성화
    document.getElementById('joinMailSelect').disabled = true;
    document.getElementById('joinMailSelect').classList.add('email-disabled');
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

window.onload = function () {

};
