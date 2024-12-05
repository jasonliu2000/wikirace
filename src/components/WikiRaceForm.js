import { useState } from 'react';
import { Button, Paper, Box, Container } from '@mui/material';

import WikiRaceInput from './WikiRaceInput';
import wikiraceServices from '../services/wikirace';

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
      console.error(error);
    }
  }

  function startButtonClicked(event) {
    event.preventDefault();
    const wikiRaceAttempted = { start: newStart, target: newTarget };
    startWikiRace(wikiRaceAttempted);
  }

  function handleFormChange(inputId, newValue) {
    switch (inputId) {
      case 'start':
        setNewStart(newValue);
        break;
      case 'target':
        setNewTarget(newValue);
        break;
      default:
        console.error('Input field with this id does not exist');
    }
  }

  function cleanupInputs() {
    setNewStart('');
    setNewTarget('');
  }

  return (
    <Container maxWidth="sm">
      <Paper elevation={3} sx={{ padding: 4, marginTop: 4 }}>
        <form 
          onSubmit={startButtonClicked} 
          aria-label="Form to input Wikirace start and target inputs"
        >
          
          <Box sx={{ marginBottom: 2 }}>
            <WikiRaceInput 
              id="start"
              newValue={newStart}
              handleFormChange={handleFormChange}
            />
          </Box>

          <Box sx={{ marginBottom: 2 }}>
            <WikiRaceInput 
              id="target"
              newValue={newTarget}
              handleFormChange={handleFormChange}
            />
          </Box>

          <Box>
            <Button 
              variant="contained"
              type="submit" 
              color="white" 
              /*disabled={newWikiRaceDisabled}*/
            >
              Start
            </Button>
          </Box>

        </form>
      </Paper>
    </Container>
  );
}

export default WikiRaceForm;