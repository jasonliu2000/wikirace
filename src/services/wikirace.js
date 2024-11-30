import axios from 'axios';

const baseURL = '/api';

async function getAll() {
  const response = await axios.get(`${baseURL}/wikiraces`);
  return response.data;
}

async function get(wikiRaceLocation) {
  const response = await axios.get(`${baseURL}${wikiRaceLocation}`)
  return response.data;
}

async function start(wikiRaceToInitiate) {
  return axios.post(`${baseURL}/wikirace`, wikiRaceToInitiate);
}

export default { getAll, get, start };