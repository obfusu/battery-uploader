import React, { Component } from 'react';
import { BatteryState } from './BatteryState';

export class Layout extends Component {
    static displayName = Layout.name;

    render() {
        return (
            <div>
                <BatteryState />
            </div>
        );
    }
}
