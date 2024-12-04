import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'https://en.wikipedia.org/w/api.php',
  });
// https://en.wikipedia.org/w/api.php?action=opensearch&search=Donald%20Trump&limit=5&namespace=0&format=json

async function searchWikipedia(query) {
	const params = {
		action: 'opensearch',
		search: query,
		limit: 5,
		namespace: 0,
		format: 'json',
		origin: '*',
	};

  const response = await apiClient.get('', {params});
  return response.data;
}

export default { searchWikipedia };