import { useState, useEffect } from 'react';

import './App.css';
import wikiraceServices from './services/wikirace'

function App() {
  const [wikiRaces, setWikiRaces] = useState([]);
  const [startingArticle, setStartingArticle] = useState("");
  const [targetArticle, setTargetArticle] = useState("");

  useEffect(() => {
    async function fetchWikiRaces() {
      const wikiRaces = await wikiraceServices.getAll();
      setWikiRaces(wikiRaces);
    }
    fetchWikiRaces(); // function called everytime the browser is loaded/reloaded
  }, []);

  function startButtonClicked(e) {
    e.preventDefault();
    console.log("wikirace started");

    const wikiRaceAttempted = { start: startingArticle, target: targetArticle };
    startWikiRace(wikiRaceAttempted);
  }

  async function startWikiRace(newWikiRace) {
    console.log(newWikiRace);
    try {
      const response = await wikiraceServices.start(newWikiRace);
      console.log(response);
    } catch (error) {
      console.log(error.response.data.error);
    }
  }

  const listWikiRaces = wikiRaces.map(wikiRace =>
    <li key={wikiRace.id}>
      <p>{wikiRace.pathToTarget} took {wikiRace.timeDurationMilliseconds} milliseconds</p>
    </li>
  );

  return (
    <div className="App">
      <h2>wikiracing</h2>

      <form onSubmit={startButtonClicked}>
        <label>
          Starting Wiki article:
          <input 
            value={startingArticle} 
            onChange={ e => setStartingArticle(e.target.value) } 
          />
        </label>

        <br></br>
        <br></br>

        <label>
          Target Wiki article:
          <input 
            value={targetArticle} 
            onChange={ e => setTargetArticle(e.target.value) } 
          />
        </label>

        <br></br>
        <br></br>

        <button type="submit">START</button>
      </form>

      <div>
        <h4>completed wikiraces</h4>
        <ul>{listWikiRaces}</ul>
      </div>

    </div>
  );
}

export default App;
