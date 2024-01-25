const url = "https://api.s2it.kro.kr/api/v1/user/staking/infos"

const getData = () => {
  const response = fetch(url);
  console.log("rrrr", response);
};

getData();
