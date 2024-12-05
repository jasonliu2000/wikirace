import { useState, useCallback } from 'react';
import { FormControl, InputLabel, OutlinedInput, Autocomplete } from '@mui/material';
import { debounce } from 'lodash';

import '../App.css';
import wikipediaServices from '../services/wikipedia';

const WikiRaceInput = ({ id, handleFormChange }) => {
	const [open, setOpen] = useState(false);
	const [inputValue, setInputValue] = useState('');
	const [options, setOptions] = useState([]);
	const [loading, setLoading] = useState(false);

	const label = (id === 'start') ? 'Enter starting page' : 'Enter target page';

  async function handleSearch(searchTerm) {
		console.log(searchTerm);
    setLoading(true);

		const articles = await wikipediaServices.searchWikipedia(searchTerm);
		const filteredOptions = articles.filter((article) => article.toLowerCase().startsWith(searchTerm.toLowerCase()));

		setOptions(filteredOptions);
		setLoading(false);
  }

	const debouncedSearch = useCallback(
		debounce((value) => {
			if (value) {
				handleSearch(value);
			} else {
				setOptions([]);
			}
		}, 250)
	, []);

	function handleInputChange(_, newInputValue) {
		handleFormChange(id, newInputValue)
		setInputValue(newInputValue);
		debouncedSearch(newInputValue);
	}

	return (
		<FormControl>
			<InputLabel>{label}</InputLabel>
			<Autocomplete
				id={id}
        open={open}
        onOpen={() => setOpen(true)}
        onClose={() => {
					setOpen(false);
					setOptions([]);
				}}
        isOptionEqualToValue={(option, value) => option.code === value.code}
        options={options}
        loading={loading}
        inputValue={inputValue}
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