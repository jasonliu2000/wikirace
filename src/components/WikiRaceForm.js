import { FormControl, InputLabel, OutlinedInput, Button } from '@mui/material';
import Grid from '@mui/material/Grid2';

const WikiRaceForm = ({ startButtonClicked, newStart, newTarget, handleInputChange, newWikiRaceDisabled }) => {
  return (
    <form onSubmit={startButtonClicked} aria-label="Form to input Wikirace start and target inputs">

        <Grid container spacing={8}>
          <Grid size={6}>
            <FormControl>
              <InputLabel>Starting page</InputLabel>
              <OutlinedInput
                placeholder="Wikiracing"
                label="Starting page"
                name="start"
                value={newStart}
                onChange={handleInputChange} 
              />
            </FormControl>
          </Grid>

          <Grid size={6}>
            <FormControl>
              <InputLabel>Target page</InputLabel>
              <OutlinedInput
                placeholder="Semisopochnoi_Island"
                label="Target page"
                name="target"
                value={newTarget} 
                onChange={handleInputChange} 
              />
            </FormControl>
          </Grid>
        </Grid>

        <Button variant="contained" type="submit" disabled={newWikiRaceDisabled}>START</Button>
        {/* <button type="submit" disabled={newWikiRaceDisabled}>START</button> {wikiRaceFailed && <p>wikirace failed!</p>} */}
      </form>
  );
}

export default WikiRaceForm;