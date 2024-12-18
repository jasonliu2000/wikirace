import { useState, useEffect } from 'react';
import { Button, Paper, Box, Container, Alert } from '@mui/material';

import WikiRaceInput from './WikiRaceInput';
import wikiraceServices from '../services/wikirace';

const WikiRaceForm = ({ onSuccess, serverError }) => {
  const [newStart, setNewStart] = useState("");
  const [newTarget, setNewTarget] = useState("");
  const [count, setCount] = useState(0);
  const [buttonDisabled, setButtonDisabled] = useState(false);

  useEffect(() => {
    disableStartButton();
  }, [serverError]);

  async function startWikiRace(newWikiRace) {
    try {
      const response = await wikiraceServices.start(newWikiRace);
      if (response.status === 202) {
        cleanupInputs();
        onSuccess(response.headers['location']);
      }
    } catch (error) {
      console.error(error);
    }
  }

  function startButtonClicked(event) {
    event.preventDefault();
    const wikiRaceAttempted = { start: newStart, target: newTarget };
    startWikiRace(wikiRaceAttempted);
  }

  function disableStartButton() {
    setButtonDisabled(serverError);
  }

  function cleanupInputs() {
    setNewStart("");
    setNewTarget("");
    setCount(prevCount => prevCount + 1);
  }

  const alertElement = () => {
    return (
      <Alert 
        severity="error"
        sx={{
          marginBottom: 2
        }}
      >
        Failed to connect to backend server.
      </Alert>
    )
  }

  return (
    <Container maxWidth="sm">
      <Paper elevation={3} sx={{ padding: 4, margin: 4 }}>
        <form 
          onSubmit={startButtonClicked} 
          aria-label="Form to input Wikirace start and target inputs"
        >

          {serverError && alertElement()}
          
          <Box sx={{ marginBottom: 2 }}>
            <WikiRaceInput 
              id="start"
              value={newStart}
              onChange={setNewStart}
              newRace={count}
            />
          </Box>

          <Box sx={{ marginBottom: 2 }}>
            <WikiRaceInput 
              id="target"
              value={newTarget}
              onChange={setNewTarget}
              newRace={count}
            />
          </Box>

          <Box>
            <Button 
              variant="contained"
              type="submit" 
              color="white"
              disabled={buttonDisabled}
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