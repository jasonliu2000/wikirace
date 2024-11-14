import axios from 'axios';

async function getAll() {
  const response = await axios.get("/api/wikiraces");
  return response.data;
}

async function start(wikiRaceToInitiate) {
  return axios.post("/api/wikirace", wikiRaceToInitiate);
}

export default { getAll, start };