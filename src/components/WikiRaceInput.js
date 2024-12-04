import * as React from 'react';
import { FormControl, InputLabel, OutlinedInput, useAutocomplete } from '@mui/material';
import { styled } from '@mui/system';

const WikiRaceInput = ({ id, newValue, handleInputChange }) => {
	const Listbox = styled('ul')(({ theme }) => ({
    width: 200,
    margin: 0,
    padding: 0,
    zIndex: 1,
    position: 'absolute',
    listStyle: 'none',
    backgroundColor: '#fff',
    overflow: 'auto',
    maxHeight: 200,
    border: '1px solid rgba(0,0,0,.25)',
    '& li.Mui-focused': {
      backgroundColor: '#4a8df6',
      color: 'white',
      cursor: 'pointer',
    },
    '& li:active': {
      backgroundColor: '#2977f5',
      color: 'white',
    },
    ...theme.applyStyles('dark', {
      backgroundColor: '#000',
    }),
  }));

	const {
    getInputProps,
    getListboxProps,
    getOptionProps,
    groupedOptions
  } = useAutocomplete({
    options: ['Greece', 'Greek language', 'Greece national football team', 'Greece\u2013Turkey relations', 'Greece in the Eurovision Song Contest'],
    // onInputChange: () => __
  });

	let placeholder = 'Wikiracing';
	let label = 'Starting page';
	let name = 'start';

	if (id === 'target') {
		placeholder = 'Semisopochnoi_Island';
		label = 'Target page';
		name = 'target';
	}

	console.log(getListboxProps());

	

	return (
		<FormControl>
			<InputLabel>{label}</InputLabel>
			<OutlinedInput {...getInputProps()}
				placeholder={placeholder}
				label={label}
				name={name}
				value={newValue}
				onChange={handleInputChange} 
				sx={{ width: '400px' }}
			/>
			{groupedOptions.length > 0 && (
				<Listbox {...getListboxProps()}>
					{groupedOptions.map((option, index) => {
						const { key, ...optionProps } = getOptionProps({ option, index });
						return (
							<li key={key} {...optionProps}>
								{option}
							</li>
						);
					})}
				</Listbox>
			)}
		</FormControl>
	);
}

export default WikiRaceInput;