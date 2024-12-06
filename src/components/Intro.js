import { Box, Typography } from '@mui/material';

import backgroundStyles from '../styles/backgroundStyles';

const Intro = () => {
	return (
		<Box sx={backgroundStyles}>
			<Typography 
				variant="h1" 
				sx={{
					fontFamily: "inherit",
					fontSize: "60px"
				}} 
				gutterBottom
				>
					Wikiracing
			</Typography>
			
      <Typography 
				variant="h6" 
				color="gray" 
				sx={{fontFamily: "inherit"}} 
				gutterBottom
			>
				Navigate from one Wikipedia article to another using only internal links, in the fastest time possible.
			</Typography>

      {/* <img src="wikiracing.png" alt="Wikiracing image"></img> */}
		</Box>
	);
}

export default Intro;