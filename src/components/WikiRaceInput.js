import { useState, useCallback } from 'react';
import { FormControl, InputLabel, OutlinedInput, Autocomplete } from '@mui/material';
import { debounce } from 'lodash';

import wikipediaServices from '../services/wikipedia';

const WikiRaceInput = ({ id, value, onChange, newRace }) => {
	const [open, setOpen] = useState(false);
	const [options, setOptions] = useState([]);
	const [loading, setLoading] = useState(false);
	const [wikiSvcDown, setWikiSvcDown] = useState(false);

	const label = (id === 'start') ? 'Enter starting page' : 'Enter target page';

	const debouncedSearch = useCallback(
		debounce((searchTerm) => {
			if (searchTerm) {
				handleSearch(searchTerm);
			} else {
				setOptions([]);
			}
		}, 250)
	, []);

	function handleInputChange(_, newValue) {
		if (value) {
			handleSearch(newValue);
		} else {
			debouncedSearch(newValue);
		}

		onChange(newValue);
	}

  async function handleSearch(searchTerm) {
		let searchError = false;
    setLoading(!wikiSvcDown);

		try {
			const articles = await wikipediaServices.searchWikipedia(searchTerm);
			const filteredOptions = articles.filter((article) => article.toLowerCase().startsWith(searchTerm.toLowerCase()));
			setOptions(filteredOptions);
		} catch (error) {
			console.error(error);
			searchError = true;
		} finally {
			setLoading(false);
			setWikiSvcDown(searchError);
		}
  }

	return (
		<FormControl>
			<InputLabel>{label}</InputLabel>
			<Autocomplete
				id={id}
				key={newRace}
        open={open && value}
				freeSolo={wikiSvcDown}
        onOpen={() => setOpen(true)}
        onClose={() => {
					setOpen(false);
					setOptions([]);
				}}
        isOptionEqualToValue={(option, value) => option.code === value.code}
        options={options}
        loading={loading}
        inputValue={value}
        onInputChange={handleInputChange}
        renderInput={(params) => (
					<OutlinedInput
						{...params.InputProps}
						{...params}
						label={label}
						endAdornment={null}
						sx={{
							width: '400px',
						}}
					/>
        )}
        renderOption={(props, option) => (
					<li {...props} key={option}>
						{option}
          </li>
        )}
        noOptionsText="Wikipedia article not found"
      />
		</FormControl>
	);
}

export default WikiRaceInput;