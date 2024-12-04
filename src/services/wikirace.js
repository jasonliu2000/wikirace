import axios from 'axios';

const apiClient = axios.create({
  baseURL: '/api',
});

async function getAll() {
  const response = await apiClient.get('/wikiraces');
  return response.data;
}

async function get(wikiRaceLocation) {
  const response = await apiClient.get(wikiRaceLocation)
  return response.data;
}

async function start(wikiRaceToInitiate) {
  return apiClient.post('/wikirace', wikiRaceToInitiate);
}

export default { getAll, get, start };