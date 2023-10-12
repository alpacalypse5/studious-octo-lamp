import React from 'react';
import { Grid, TextField as MuiTextField } from '@mui/material';

const AddressFields = ({ prefix, formValues, handleChange, isEditing }) => {
    return (
        <>
            {['lineOne', 'lineTwo', 'city', 'state', 'zipCode', 'country'].map(field => (
                <Grid item xs={6} key={field}>
                    <MuiTextField 
                        label={field.charAt(0).toUpperCase() + field.slice(1)}
                        name={`${prefix}.${field}`}
                        value={formValues[prefix]?.[field] || ''}
                        onChange={handleChange}
                        disabled={!isEditing}
                        variant="outlined"
                        fullWidth
                    />
                </Grid>
            ))}
        </>
    );
};

export default AddressFields;
