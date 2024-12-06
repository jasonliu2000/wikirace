import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'https://en.wikipedia.org/w/api.php',
  });

async function searchWikipedia(query) {
	const params = {
		action: 'opensearch',
		search: query,
		limit: 5,
		namespace: 0,
		format: 'json',
		origin: '*',
	};

	try {
		const response = await apiClient.get('', {params});

		if (!response || !response.data || !Array.isArray(response.data)) {
			throw new Error('Unexpected API response format from Wikipedia');
		}

		const results = response.data[11];

		if (!Array.isArray(results)) {
      throw new Error('Failed to find search results from API response');
    }

		return results;

	} catch (error) {
    if (error.code === 'ECONNABORTED') {
      throw new Error('Wikipedia search timed out');
    }

    if (error.response) {
      throw new Error(`Wikipedia API error: ${error.response.status}`);
    } else if (error.request) {
      throw new Error('No response received from Wikipedia');
    } else {
      throw new Error(`Error retrieving autocomplete options from Wikipedia: ${error.message}`);
    }
  }
}

export default { searchWikipedia };