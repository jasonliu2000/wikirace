import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL || ''; // if unset, the proxy address will be used by default in package.json

const apiClient = axios.create({
  baseURL: `${API_URL}/api`,
});

async function getAll() {
  const response = await apiClient.get('/wikiraces');
  return response.data;
}

async function get(wikiRaceLocation) {
  const response = await apiClient.get(wikiRaceLocation);
  return response.data;
}

async function start(wikiRaceToInitiate) {
  return apiClient.post('/wikirace', wikiRaceToInitiate);
}

export default { getAll, get, start };