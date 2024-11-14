import { http, HttpResponse } from 'msw';

export const handlers = [

  http.get('/api/wikiraces', () => {
    console.log("Captured a GET /api/wikiraces request");
    return HttpResponse.json([
      {
        "id": 1,
        "status": "Wikirace has completed.",
        "timeDurationMilliseconds": "80514",
        "pathToTarget": ["Wikiracing","United_States","Alaska","Semisopochnoi_Island"]
      },
      {
        "id": 2,
        "status": "Wikirace has completed.",
        "timeDurationMilliseconds": "1790",
        "pathToTarget": ["Wikiracing","United_States","Alaska"]
      },
    ]);
  }),

  http.post('/api/wikirace', () => {
    console.log("Captured a POST /api/wikirace request");
    return new HttpResponse(null, {
      status: 202,
      headers: {
        'Content-Type': 'text/plain',
        'Location': '/wikirace/1',
      },
      statusText: 'Response accepted...',
    })
  }),
];
