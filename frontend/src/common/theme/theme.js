import { alpha, createTheme } from '@mui/material/styles';

const slate = '#172033';
const blue = '#0f8fce';
const green = '#16a673';

const theme = createTheme({
    palette: {
        mode: 'light',
        primary: {
            main: blue,
            light: '#d9f1fb',
            dark: '#096b9e',
            contrastText: '#ffffff'
        },
        secondary: {
            main: green,
            light: '#dff6ee',
            dark: '#0d7652',
            contrastText: '#ffffff'
        },
        error: {
            main: '#d92d20',
            light: '#fff3f0',
            dark: '#9b1c14'
        },
        warning: {
            main: '#d08b19',
            light: '#fff6df',
            dark: '#925f0c'
        },
        success: {
            main: green,
            light: '#e7f8f1',
            dark: '#0d7652'
        },
        background: {
            default: '#f4f8fb',
            paper: '#ffffff'
        },
        text: {
            primary: slate,
            secondary: '#65748b'
        },
        divider: 'rgba(23, 32, 51, 0.1)'
    },
    typography: {
        fontFamily: '"Inter", "Segoe UI", Roboto, Helvetica, Arial, sans-serif',
        h1: {fontWeight: 800, letterSpacing: 0},
        h2: {fontWeight: 800, letterSpacing: 0},
        h3: {fontWeight: 800, letterSpacing: 0},
        h4: {fontWeight: 800, letterSpacing: 0},
        h5: {fontWeight: 800, letterSpacing: 0},
        h6: {fontWeight: 750, letterSpacing: 0},
        button: {
            textTransform: 'none',
            fontWeight: 750,
            letterSpacing: 0
        }
    },
    shape: {
        borderRadius: 12
    },
    components: {
        MuiCssBaseline: {
            styleOverrides: {
                body: {
                    backgroundColor: '#f4f8fb',
                    color: slate
                }
            }
        },
        MuiPaper: {
            styleOverrides: {
                root: {
                    backgroundImage: 'none',
                    borderColor: 'rgba(23, 32, 51, 0.1)'
                },
                elevation1: {
                    boxShadow: '0 10px 30px rgba(23, 32, 51, 0.08)'
                },
                elevation2: {
                    boxShadow: '0 16px 42px rgba(23, 32, 51, 0.1)'
                },
                elevation3: {
                    boxShadow: '0 22px 60px rgba(23, 32, 51, 0.12)'
                }
            }
        },
        MuiCard: {
            styleOverrides: {
                root: {
                    borderRadius: 14,
                    border: '1px solid rgba(23, 32, 51, 0.1)',
                    boxShadow: '0 18px 44px rgba(23, 32, 51, 0.09)',
                    overflow: 'hidden'
                }
            }
        },
        MuiCardContent: {
            styleOverrides: {
                root: {
                    padding: 26,
                    '&:last-child': {
                        paddingBottom: 26
                    }
                }
            }
        },
        MuiButton: {
            defaultProps: {
                disableElevation: true
            },
            styleOverrides: {
                root: {
                    minHeight: 42,
                    borderRadius: 10,
                    paddingInline: 18,
                    whiteSpace: 'nowrap'
                },
                containedPrimary: {
                    background: `linear-gradient(135deg, ${blue} 0%, #2563eb 100%)`,
                    boxShadow: '0 12px 24px rgba(15, 143, 206, 0.22)',
                    '&:hover': {
                        background: 'linear-gradient(135deg, #0b7fb7 0%, #1d4ed8 100%)',
                        boxShadow: '0 14px 30px rgba(15, 143, 206, 0.28)'
                    }
                },
                containedSecondary: {
                    background: `linear-gradient(135deg, ${green} 0%, #0f8fce 100%)`,
                    boxShadow: '0 12px 24px rgba(22, 166, 115, 0.2)'
                },
                outlined: {
                    borderColor: 'rgba(23, 32, 51, 0.16)'
                }
            }
        },
        MuiTextField: {
            defaultProps: {
                variant: 'outlined'
            }
        },
        MuiOutlinedInput: {
            styleOverrides: {
                root: {
                    borderRadius: 12,
                    backgroundColor: '#ffffff',
                    transition: 'box-shadow 0.2s ease, background-color 0.2s ease, border-color 0.2s ease',
                    '&:hover .MuiOutlinedInput-notchedOutline': {
                        borderColor: alpha(blue, 0.55)
                    },
                    '&.Mui-focused': {
                        boxShadow: `0 0 0 4px ${alpha(blue, 0.12)}`
                    },
                    '&.Mui-focused .MuiOutlinedInput-notchedOutline': {
                        borderColor: blue,
                        borderWidth: 1
                    }
                },
                notchedOutline: {
                    borderColor: 'rgba(23, 32, 51, 0.14)'
                }
            }
        },
        MuiInputLabel: {
            styleOverrides: {
                root: {
                    color: '#65748b'
                }
            }
        },
        MuiTableCell: {
            styleOverrides: {
                head: {
                    color: '#42526a',
                    fontSize: '0.74rem',
                    fontWeight: 800,
                    letterSpacing: 0,
                    textTransform: 'uppercase',
                    backgroundColor: '#eef6fa',
                    paddingTop: 14,
                    paddingBottom: 14
                },
                root: {
                    borderBottomColor: 'rgba(23, 32, 51, 0.08)',
                    paddingTop: 15,
                    paddingBottom: 15
                }
            }
        },
        MuiTableRow: {
            styleOverrides: {
                root: {
                    '&.Mui-selected': {
                        backgroundColor: alpha(blue, 0.08)
                    },
                    '&.Mui-selected:hover': {
                        backgroundColor: alpha(blue, 0.12)
                    }
                }
            }
        },
        MuiChip: {
            styleOverrides: {
                root: {
                    borderRadius: 9,
                    fontWeight: 700
                }
            }
        },
        MuiAlert: {
            styleOverrides: {
                root: {
                    borderRadius: 10,
                    border: '1px solid rgba(23, 32, 51, 0.08)'
                }
            }
        },
        MuiDialog: {
            styleOverrides: {
                paper: {
                    borderRadius: 18,
                    boxShadow: '0 24px 70px rgba(23, 32, 51, 0.18)'
                }
            }
        },
        MuiTabs: {
            styleOverrides: {
                root: {
                    minHeight: 54
                },
                indicator: {
                    height: 3,
                    borderRadius: 3
                }
            }
        },
        MuiTab: {
            styleOverrides: {
                root: {
                    minHeight: 54,
                    fontWeight: 750,
                    textTransform: 'none'
                }
            }
        },
        MuiMenu: {
            styleOverrides: {
                paper: {
                    borderRadius: 12,
                    border: '1px solid rgba(23, 32, 51, 0.08)',
                    boxShadow: '0 18px 42px rgba(23, 32, 51, 0.14)'
                }
            }
        }
    }
});

export default theme;
