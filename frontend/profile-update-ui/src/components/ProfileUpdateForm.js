import React, { useState, useEffect, useCallback } from 'react';
import { Button, Container, CssBaseline, Typography, Box, TextField as MuiTextField, Grid, Divider } from '@mui/material';
import AddressFields from '../helper/AddressFields';
import UseProfileForm from '../helper/UseProfileForm';

function ProfileUpdateForm() {
    const { isEditing, setIsEditing, isLoading, apiError, formValues, handleChange, handleSubmit } = UseProfileForm();

    return (
      <Container component="main" maxWidth="md">
      <CssBaseline />
      <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        pt: 8,
      }}
    >
      <Typography component="h1" variant="h5">
        Business Profile
      </Typography>
      <Button
        variant="outlined"
        color="primary"
        onClick={() => setIsEditing(!isEditing)}
        style={{ marginBottom: 20 }}
      >
        {isEditing ? 'Cancel' : 'Edit'}
      </Button>
      <form onSubmit={handleSubmit} style={{ width: '100%', marginTop: 3 }}>
    <Grid container spacing={2}>
      <Grid item xs={6}>
        <MuiTextField 
          required 
          label="Company Name" 
          name="companyName" 
          value={formValues.companyName || ''} 
          onChange={handleChange} 
          disabled={!isEditing} 
          variant="outlined" 
          fullWidth
        />
      </Grid>
      
      <Grid item xs={6}>
        <MuiTextField 
          required 
          label="Legal Name" 
          name="legalName" 
          value={formValues.legalName || ''} 
          onChange={handleChange} 
          disabled={!isEditing} 
          variant="outlined" 
          fullWidth
        />
      </Grid>

      <Grid item xs={6}>
        <MuiTextField 
          required 
          label="Email" 
          name="email" 
          value={formValues.email || ''} 
          onChange={handleChange} 
          disabled={!isEditing} 
          variant="outlined" 
          fullWidth
        />
      </Grid>

      <Grid item xs={6}>
        <MuiTextField 
          label="Website" 
          name="website" 
          value={formValues.website || ''} 
          onChange={handleChange} 
          disabled={!isEditing} 
          variant="outlined" 
          fullWidth
        />
      </Grid>
      <Grid item xs={12}>
            <Divider sx={{ my: 2 }} />
            <Typography variant="h6">Business Address</Typography>
          </Grid>
          <AddressFields prefix="businessAddress" formValues={formValues} handleChange={handleChange} isEditing={isEditing} />
          <Grid item xs={12}>
            <Divider sx={{ my: 2 }} />
            <Typography variant="h6">Legal Address</Typography>
          </Grid>
          <AddressFields prefix="legalAddress" formValues={formValues} handleChange={handleChange} isEditing={isEditing} />


      {isEditing && (
        <Button
          type="submit"
          fullWidth
          variant="contained"
          color="primary"
          style={{ marginTop: 3 }}
        >
          Submit
        </Button>
      )}
      {apiError && <p style={{ color: 'red' }}>{apiError}</p>}
    </Grid>
  </form>
    </Box>
  </Container>
    );
}

export default ProfileUpdateForm;
