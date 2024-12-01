import { useState, useEffect } from 'react';

import './App.css';
import wikiraceServices from './services/wikirace'

function App() {
  const [wikiRaces, setWikiRaces] = useState([]);
  const [startingArticle, setStartingArticle] = useState("");
  const [targetArticle, setTargetArticle] = useState("");
  const [newWikiRaceDisabled, setNewWikiRaceDisabled] = useState(false);
  const [wikiRaceFailed, setWikiRaceFailed] = useState(false);

  useEffect(() => {
    fetchWikiRaces(); // function called everytime the browser is loaded/reloaded
  }, []);

  async function fetchWikiRaces() {
    const wikiRaces = await wikiraceServices.getAll();
    console.log(wikiRaces);
    setWikiRaces(wikiRaces);
  }

  async function startWikiRace(newWikiRace) {
    try {
      const response = await wikiraceServices.start(newWikiRace);
      if (response.status === 202) {
        setNewWikiRaceDisabled(true);
        fetchWikiRaces();
      }

      pollWikiRaceProgress(response.headers['location']);

    } catch (error) {
      setWikiRaceFailed(true);
      console.log(error.toJSON());
    }
  }

  async function pollWikiRaceProgress(wikiRaceId) {
    const polling = new Promise((resolve) => {
      let completed = false;
      setInterval(async () => {
        if (completed) { return }

        const response = await wikiraceServices.get(wikiRaceId);
        console.log(response);
        
        if (response.status === 'COMPLETED') {
          completed = true;
          clearInterval(polling);
          resolve('')
        } 
      }, 500)
    })
    
    const completed = await polling.catch((err) => {
      console.error(err);
      return 'there was an issue with completing the wikirace';
    });
    
    setNewWikiRaceDisabled(false);
  }

  function startButtonClicked(e) {
    e.preventDefault();
    setWikiRaceFailed(false);

    const wikiRaceAttempted = { start: startingArticle, target: targetArticle };
    startWikiRace(wikiRaceAttempted);
  }

  const listWikiRaces = wikiRaces.map(wikiRace =>
    <li key={wikiRace.id}>
      <p>{wikiRace.data.pathToTarget} took {wikiRace.data.elapsedTimeMilliseconds} milliseconds</p>
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

        <button type="submit" disabled={newWikiRaceDisabled}>START</button> {wikiRaceFailed && <p>wikirace failed!</p>}
      </form>

      <div>
        <h4>completed wikiraces</h4>
        <ul>{listWikiRaces}</ul>
      </div>

    </div>
  );
}

export default App;
