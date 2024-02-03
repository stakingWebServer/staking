const API_KEY = "";
var today_register = "";
var today_login_user = "";
var today_drop_user = "";

async function todayRegister() {
  let url = new URL(`https://api.s2it.kro.kr/api/v1/admin/today-register`);
  const response = await fetch(url,{mode:'cors', credentials:'include', headers : {
      Authorization: `Bearer ${localStorage.getItem("accessKey")}`
    }});
  data = await response.json();
  console.log("ddd", data.result.todayRegister);
  today_register = data.result.todayRegister;
  console.log(today_register);
  render();
}

async function todayLoginUser() {
  let url = new URL(`https://api.s2it.kro.kr/api/v1/admin/today-loginUser`);
  const response = await fetch(url,{mode:'cors', credentials:'include',headers : {
      Authorization: `Bearer ${localStorage.getItem("accessKey")}`
    }});
  data = await response.json();
  console.log("ddd", data.result.todayLoginUser);
  today_login_user = data.result.todayLoginUser;
  console.log(today_login_user);
  render();
}

async function todayDropUser() {
  let url = new URL(`https://api.s2it.kro.kr/api/v1/admin/today-dropUser`);
  const response = await fetch(url,{mode:'cors', credentials:'include',headers : {
      Authorization: `Bearer ${localStorage.getItem("accessKey")}`
    }});
  data = await response.json();
  today_drop_user = data.result.todayDropUser;
  render();
}

async function todayDropUser() {
  let url = new URL(`https://api.s2it.kro.kr/api/v1/admin/today-dropUser`);
  const response = await fetch(url,{mode:'cors', credentials:'include',headers : {
      Authorization: `Bearer ${localStorage.getItem("accessKey")}`
    }});
  data = await response.json();
  today_drop_user = data.result.todayDropUser;
  render();
}

render = () => {
  //   console.log(document.getElementById("todayRegister").innerHTML);
  document.getElementById("today_register").innerHTML = today_register;
  document.getElementById("today_login_user").innerHTML = today_login_user;
  document.getElementById("today_drop_user").innerHTML = today_drop_user;
};

todayRegister();
todayLoginUser();
todayDropUser();
