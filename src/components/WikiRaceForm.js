import { useState } from 'react';
import { Button } from '@mui/material';
import Grid from '@mui/material/Grid2';


import WikiRaceInput from './WikiRaceInput';
import wikiraceServices from '../services/wikirace';
import wikipediaServices from '../services/wikipedia';

const WikiRaceForm = ({ followWikiRace }) => {
  const [newStart, setNewStart] = useState("");
  const [newTarget, setNewTarget] = useState("");
  // const [newWikiRaceDisabled, setNewWikiRaceDisabled] = useState(false);

  async function startWikiRace(newWikiRace) {
    try {
      const response = await wikiraceServices.start(newWikiRace);
      if (response.status === 202) {
        // setNewWikiRaceDisabled(true);
        cleanupInputs();
        followWikiRace(response.headers['location']);
      }
    } catch (error) {
      // setWikiRaceFailed(true);
      console.log(error);
    }
  }

  function startButtonClicked(event) {
    event.preventDefault();

    const wikiRaceAttempted = { start: newStart, target: newTarget };
    startWikiRace(wikiRaceAttempted);
  }

  async function getSuggestedArticles(inputValue) {
    if (inputValue) {
      const suggestedArticles = await wikipediaServices.searchWikipedia(inputValue);
      console.log(suggestedArticles);
    }
  }

  function handleInputChange(event) {
    const changedValue = event.target.value;
    // getSuggestedArticles(changedValue);
    switch (event.target.name) {
      case 'start':
        setNewStart(changedValue);
        break;
      case 'target':
        setNewTarget(changedValue);
        break;
      default:
        console.error('Input field doesn\'t have a name field');
    }
  }

  function cleanupInputs() {
    setNewStart("");
    setNewTarget("");
  }

  return (
    <form onSubmit={startButtonClicked} aria-label="Form to input Wikirace start and target inputs">

        <Grid container spacing={8}>
          <Grid size={5}>
            <WikiRaceInput 
              id="start"
              newValue={newStart}
              handleInputChange={handleInputChange}
            />
          </Grid>

          <Grid size={5}>
            <WikiRaceInput 
              id="target"
              newValue={newTarget}
              handleInputChange={handleInputChange}
            />
          </Grid>

          <Grid size={2}>
            <Button variant="contained" type="submit" /*disabled={newWikiRaceDisabled}*/>START</Button>
          </Grid>
        </Grid>

      </form>
  );
}

export default WikiRaceForm;