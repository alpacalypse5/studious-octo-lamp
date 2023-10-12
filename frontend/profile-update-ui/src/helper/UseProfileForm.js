import { useState, useEffect, useCallback } from 'react';

const UseProfileForm = () => {
    const [isEditing, setIsEditing] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [apiError, setApiError] = useState(null);
    const [formValues, setFormValues] = useState({
        companyName: '',
        legalName: '',
        businessAddress: {
        lineOne: '',
        lineTwo: '',
        city: '',
        state: '',
        zipCode: '',
        country: '',
        },
        legalAddress: {
            lineOne: '',
            lineTwo: '',
            city: '',
            state: '',
            zipCode: '',
            country: '',
        },
        taxIdentifiers: {
        PAN: '',
        EIN: '',
        },
        email: '',
        website: '',
    });

    useEffect(() => {
        const fetchInitialData = async () => {
          try {
            const response = await fetch('http://localhost:8080/business-profile', {
                method: 'GET',
                headers: {
                    'X-Business-Id': 'business-id-5'
                }
            });
    
            if (!response.ok) {
              const data = await response.json();
              throw new Error(data.message || 'Something went wrong');
            }
    
            const data = await response.json();
            setFormValues(data);
          } catch (error) {
            setApiError(error.message);
          } finally {
            setIsLoading(false);
          }
        };
    
        fetchInitialData();
      }, []); 
    
    
      const handleChange = useCallback((e) => {
        const { name, value } = e.target;
        const keys = name.split('.');
      
        if (keys.length > 1) {
          setFormValues(prevState => ({
            ...prevState,
            [keys[0]]: {
              ...prevState[keys[0]],
              [keys[1]]: value
            }
          }));
        } else {
          setFormValues(prevState => ({ ...prevState, [name]: value }));
        }
        }, []);
    
    
      const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            setIsLoading(true);
            delete formValues.id;
            const response = await fetch('http://localhost:8080/business-profile/event', {
                method: 'POST',
                headers: {
                    'X-Business-Id': 'business-id-5',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    payload: formValues,
                    source: "ACCOUNTING",
                    eventType: "UPDATE_PROFILE"
                    
                }),
            });
    
            if (!response.ok) {
              const data = await response.json();
              throw new Error(data.message || 'Something went wrong');
            }
          } catch (error) {
            setApiError(error.message);
          } finally {
            setIsLoading(false);
            setIsEditing(false);
          }
      };

    return { isEditing, setIsEditing, isLoading, apiError, formValues, handleChange, handleSubmit };
};

export default UseProfileForm;