import React, { Component } from 'react';
import { Route } from 'react-router';
import { BatteryState } from './components/BatteryState';
import { Layout } from './components/Layout';
import './custom.css';


export default class App extends Component {
    static displayName = App.name;

    render() {
        return (
            <Layout>
                <Route exact path='/app' component={BatteryState} />
            </Layout>
        );
    }
}
