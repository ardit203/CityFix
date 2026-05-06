import React from 'react';
import {Tooltip, Typography} from '@mui/material';

export const renderTruncatedText = (text, maxLength = 55) => {
    if (!text) return <Typography color="text.secondary">N/A</Typography>;

    if (text.length <= maxLength) return text;

    return (
        <Tooltip title={text} arrow placement="top">
            <span style={{cursor: 'help', borderBottom: '1px dotted #888'}}>
                {`${text.substring(0, maxLength)}...`}
            </span>
        </Tooltip>
    );
};
