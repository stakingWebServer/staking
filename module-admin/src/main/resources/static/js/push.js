const sendPush = () => {
    const email = document.querySelector("#email").value
    const title = document.querySelector("#title").value
    const content = document.querySelector("#content").value

    fetch('https://api.s2it.kro.kr/api/v1/admin/send-pushs', {
	    method: 'POST',
	    headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem("accessKey")}`
	    },
	    body: JSON.stringify({
            title: "Push Test Title",
            content: "Push Test Content"
        })
	})
	.then(response => {
		console.log(response)
	    if (!response.ok) {
	        throw new Error('Network response was not ok');
	    }
	    return response.json();
	})
	.then(data => {
        console.log(data)
	})
	.catch(error => {
	    console.error('Error:', error); // 에러 발생 시 콘솔에 에러를 출력합니다.
	});

}

document.querySelector("#push_btn").addEventListener('click', ()=> {
    sendPush()
})