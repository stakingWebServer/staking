const apiUrl = 'https://api.s2it.kro.kr/api/v1/admin/auth'; // API 엔드포인트 URL
const loginId = document.getElementById("login_id")
const password = document.getElementById("login_pw")
const loginBtn = document.getElementById("login_btn")
console.log("Hi")

loginBtn.addEventListener('click', ()=>{
    console.log(loginId.value)
	const requestData = {
	    loginId: loginId.value,
        password: password.value
        // loginId : "admin",
        // password : "stakingadminuser1234"
	}; // 요청할 데이터 객체
	console.log(JSON.stringify(requestData))
	fetch('https://api.s2it.kro.kr/api/v1/admin/auth', {
	    method: 'POST',
	    headers: {
	        'Content-Type': 'application/json', // JSON 형식으로 데이터를 전송할 것이므로 Content-Type을 application/json으로 설정합니다.
	    },
	    body: JSON.stringify(requestData) // 요청 데이터 객체를 JSON 문자열로 변환하여 전송합니다.
	})
	.then(response => {
		console.log(response)
	    if (!response.ok) {
	        throw new Error('Network response was not ok'); // 응답이 성공적이지 않으면 에러를 던집니다.
	    }
	    return response.json(); // JSON 형식으로 응답 데이터를 파싱하여 반환합니다.
	})
	.then(data => {
        localStorage.setItem("accessKey", data.result.accessKey);
        location.replace('/index');
	})
	.catch(error => {
	    console.error('Error:', error); // 에러 발생 시 콘솔에 에러를 출력합니다.
	});

})